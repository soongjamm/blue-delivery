package com.bluedelivery.shop.application;

import java.util.List;

import com.bluedelivery.shop.domain.Shop;

public interface ShopExposeService {
    List<Shop> getShopsByCategory(Long categoryId);

    List<Shop> getTotalOrdersRanking();
}
