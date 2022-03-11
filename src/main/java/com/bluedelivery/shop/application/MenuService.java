package com.bluedelivery.shop.application;

import com.bluedelivery.menu.domain.Menu;
import com.bluedelivery.menu.interfaces.RegisterMenuDto;

public interface MenuService {

    void registerMenu(RegisterMenuDto dto);

    void setMainMenu(Long id);

    void updateMenuStatus(Long id, Menu.MenuStatus status);

    void deleteMenu(Long id);

    boolean duplicateMenuName(String name, Long id);

    void validateMainMenu(Long groupId);

    void mainMenuSizeOver();
}
