package com.api.trip.common.scheduler;


import com.api.trip.common.naverapi.NaverApiService;
import com.api.trip.common.naverapi.NaverClient;
import com.api.trip.common.naverapi.dto.ShoppingItem;
import com.api.trip.common.naverapi.dto.ShoppingRequest;
import com.api.trip.common.naverapi.dto.ShoppingResponse;
import com.api.trip.common.sse.SseNotificationResponse;
import com.api.trip.common.sse.emitter.SseEmitterMap;
import com.api.trip.domain.item.controller.dto.CreateItemRequest;
import com.api.trip.domain.item.model.Item;
import com.api.trip.domain.item.service.ItemService;
import com.api.trip.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RequiredArgsConstructor
@Component
@EnableScheduling
@Slf4j
public class SchedulerRunner {

    private final NaverClient naverClient;
    private final NaverApiService naverApiService;
    private final ItemService itemService;
    private final NotificationService notificationService;
    private final SseEmitterMap sseEmitterMap;
    /**
     * @Description
     * 5분에 한번씩 api를 요청하여 데이터 갱신(중단조건에 걸릴때까지 50개씩 반복요청)
     * 데이터가 중복됐을 때(이미 존재하는 데이터)를 중단조건
     */
    @Scheduled(fixedDelay = 300000)
    public void updateData()
    {

        int i = 0;
        log.info("데이터 갱신 시작");
        try {

            while(true){
                ShoppingRequest request = ShoppingRequest.builder()
                        .start(1 + i * 50)
                        .display(50).build();
                ShoppingResponse searchResponse = naverClient.search(request);
                List<ShoppingItem> shoppingItems = naverApiService.doFilterCategory(searchResponse.getItems());
                List<CreateItemRequest> createItemRequests = naverApiService.toCreateItemRequest(shoppingItems);
                for (CreateItemRequest createItemRequest : createItemRequests) {
                    Item item = itemService.createItem(createItemRequest);
                    notificationService.createNotification(item, createItemRequest.getTagNames());
                    sseEmitterMap.sendToAll("notification",new SseNotificationResponse(item.getId(), createItemRequest.getTagNames()));
                    /**
                     *
                     * 알림이 가져야할 데이터가 itemId, memberId
                     * SSE보낼 때는 Object로 태그 넘기기
                     */
                    /**
                     * 백
                     *  1. 아이템의 태그를 다 뽑아옴
                     *  2. 태그를 구독한 member를 다 뽑아오는 로직
                     *  3. 회원이 10000 -> "제주"
                     *
                     *  알림들의 id를 넘겨줄텐데
                     *
                     *  server ||
                     *
                     *  member1:"제주" ->  1
                     *  member2:"제주" ->  2
                     *  member3:"제주" ->  3
                     *  member4:"제주" ->  4
                     *
                     *  member1브라우저  ||  member1:"제주" ->  tagNames = ["제주","서울], ids = [ 1,2,3,4]
                     *  member2브라우저  ||  member1:"제주" ->  1,2,3,4
                     */

                }
                i++;

            }
        }catch (DataIntegrityViolationException e)
        {
            log.info("데이터 중복 - 데이터 갱신 종료");
        }
        catch (HttpClientErrorException e)
        {
            log.info("요청 범위 초과 - 데이터 갱신 종료");
        }



    }
}
