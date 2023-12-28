package com.api.trip.common;


import com.api.trip.common.naverapi.NaverApiService;
import com.api.trip.common.naverapi.NaverClient;
import com.api.trip.common.naverapi.dto.ShoppingItem;
import com.api.trip.common.naverapi.dto.ShoppingRequest;
import com.api.trip.common.naverapi.dto.ShoppingResponse;
import com.api.trip.domain.item.controller.dto.CreateItemRequest;
import com.api.trip.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
@EnableScheduling
@Slf4j
public class schedulerRunner {

    private final NaverClient naverClient;
    private final NaverApiService naverApiService;
    private final ItemService itemService;

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
                System.out.println("==========================");
                ShoppingRequest request = ShoppingRequest.builder()
                        .start(1 + i * 50)
                        .display(50).build();
                ShoppingResponse searchResponse = naverClient.search(request);
                List<ShoppingItem> shoppingItems = naverApiService.doFilterCategory(searchResponse.getItems());
                List<CreateItemRequest> createItemRequests = naverApiService.toCreateItemRequest(shoppingItems);
                for (CreateItemRequest createItemRequest : createItemRequests) {
                    itemService.createItem(createItemRequest);
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
