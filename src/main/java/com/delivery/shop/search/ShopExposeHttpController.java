package com.delivery.shop.search;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.shop.category.GetShopsByCategoryResponse;
import com.delivery.shop.shop.Shop;

@RestController
public class ShopExposeHttpController implements ShopExposeController {
    
    private final ShopExposeService shopExposeService;
    
    public ShopExposeHttpController(ShopExposeService shopExposeService) {
        this.shopExposeService = shopExposeService;
    }
    
    @GetMapping("/{id}/shops")
    public ResponseEntity<GetShopsByCategoryResponse> getShopsByCategory(@PathVariable("id") Long id) {
        LocalDateTime when = LocalDateTime.now();
        List<Shop> shops = shopExposeService.getShopsByCategory(new SearchShopByCategoryParam(id, when));
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetShopsByCategoryResponse(shops.stream()
                        // Shop List -> DTO List
                        .map(shop -> new SearchedShopData(
                                shop.getId(),
                                shop.getName(),
                                shop.isOpeningAt(when)))
                        .collect(Collectors.toList()))
                );
    }
}