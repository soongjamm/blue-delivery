package com.bluedelivery.order.application;

import static com.bluedelivery.order.domain.ExceptionMessage.*;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bluedelivery.menu.domain.Menu;
import com.bluedelivery.menu.domain.MenuRepository;
import com.bluedelivery.order.domain.Order;
import com.bluedelivery.shop.domain.Shop;
import com.bluedelivery.shop.domain.ShopRepository;
import com.bluedelivery.user.domain.User;
import com.bluedelivery.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OrderValidator {
    
    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    
    public void validate(Order order) {
        List<Menu> menus = getMenus(order.getOrderItemIds());
        Shop shop = getShop(order.getShopId());
        getUser(order.getUserId());
        
        shop.isOrderPossible(order);
        order.isValidMenu(menus);
    }
    
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_USER_DOES_NOT_EXIST));
    }
    
    private Shop getShop(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException(SHOP_DOES_NOT_EXIST));
    }
    
    private List<Menu> getMenus(List<Long> menuIds) {
        return menuRepository.findAllById(menuIds);
    }
    
}
