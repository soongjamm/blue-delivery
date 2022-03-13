package com.bluedelivery.shop.application;


import com.bluedelivery.menu.interfaces.MenuOptionDto;
import com.bluedelivery.menu.interfaces.MenuOptionGroupDto;

public interface MenuOptionService {

    void registerMenuOptionGroup(MenuOptionGroupDto dto);

    void registerMenuOption(MenuOptionDto dto);
}
