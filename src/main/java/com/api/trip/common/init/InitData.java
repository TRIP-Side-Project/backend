package com.api.trip.common.init;

import com.api.trip.common.naverclient.NaverClient;
import com.api.trip.common.naverclient.dto.ShoppingItem;
import com.api.trip.common.naverclient.dto.ShoppingRequest;
import com.api.trip.common.naverclient.dto.ShoppingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData implements ApplicationRunner {

    private final NaverClient naverClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
/*

        ShoppingRequest request = ShoppingRequest.builder()
                .start(1)
                .display(10).build();

        ShoppingResponse searchResponse = naverClient.search(request);
        List<ShoppingItem> items = searchResponse.getItems();
        for(ShoppingItem item : items)
            System.out.println(item.toString());

*/
    }
}