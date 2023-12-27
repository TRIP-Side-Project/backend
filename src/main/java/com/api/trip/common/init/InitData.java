package com.api.trip.common.init;

import com.api.trip.common.naverapi.NaverApiService;
import com.api.trip.common.naverapi.NaverClient;
import com.api.trip.common.naverapi.dto.ShoppingItem;
import com.api.trip.common.naverapi.dto.ShoppingRequest;
import com.api.trip.common.naverapi.dto.ShoppingResponse;
import com.api.trip.domain.item.controller.dto.CreateItemRequest;
import com.api.trip.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData implements ApplicationRunner {

    private final NaverClient naverClient;
    private final NaverApiService naverApiService;
    private final ItemService itemService;

    @Override
    public void run(ApplicationArguments args) throws Exception {


/*


        for(int i = 0; i < 2; i++) {

            ShoppingRequest request = ShoppingRequest.builder()
                    .start(1 + i * 100)
                    .display(100).build();

            ShoppingResponse searchResponse = naverClient.search(request);
            List<ShoppingItem> shoppingItems = naverApiService.doFilterCategory(searchResponse.getItems());
            List<CreateItemRequest> createItemRequests = naverApiService.toCreateItemRequest(shoppingItems);

            for (CreateItemRequest createItemRequest : createItemRequests)
                itemService.createItem(createItemRequest);



        }
*/
    }
}