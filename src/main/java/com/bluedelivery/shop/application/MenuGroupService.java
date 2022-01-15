package com.bluedelivery.shop.application;

import com.bluedelivery.menu.interfaces.RegisterMenuGroupDto;
import com.bluedelivery.menu.interfaces.UpdateMenuGroupDto;
import com.bluedelivery.menu.domain.MenuGroup;

public interface MenuGroupService {

    MenuGroup registerMenuGroup(RegisterMenuGroupDto dto);

    void updateMenuGroup(UpdateMenuGroupDto dto);

    void deleteMenuGroup(Long id);

}
