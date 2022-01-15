package com.bluedelivery.shop.application.adapter;

import static com.bluedelivery.common.response.ErrorCode.*;
import static com.bluedelivery.menu.domain.Menu.*;

import com.bluedelivery.shop.application.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluedelivery.menu.interfaces.RegisterMenuDto;
import com.bluedelivery.common.response.ApiException;
import com.bluedelivery.menu.domain.Menu;
import com.bluedelivery.menu.domain.MenuGroup;
import com.bluedelivery.menu.domain.MenuGroupRepository;
import com.bluedelivery.menu.domain.MenuRepository;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    private final MenuGroupRepository menuGroupRepository;

    public MenuServiceImpl(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void registerMenu(RegisterMenuDto dto) {
        Menu menu = dto.toEntity();
        MenuGroup getMenuGroup = menuGroupRepository.findById(dto.getMenuGroupId())
                .orElseThrow(() -> new ApiException(MENU_GROUP_NOT_FOUND));

        if (duplicateMenuName(dto.getName(), dto.getMenuGroupId())) {
            throw new ApiException(MENU_ALREADY_EXISTS);
        }
        menu.setMenuGroup(getMenuGroup);

        menuRepository.save(menu);
    }

    @Transactional
    public void setMainMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ApiException(MENU_NOT_FOUND));

        if (menu.isMain() == false) {
            menu.setMain(true);
        } else {
            menu.setMain(false);
        }
        validateMainMenu(menu.getMenuGroup().getId());
    }

    @Transactional
    public void updateMenuStatus(Long id, MenuStatus status) {
        Menu target = menuRepository.findById(id)
                .orElseThrow(() -> new ApiException(MENU_NOT_FOUND));

        target.setStatus(status);
    }

    public void deleteMenu(Long id) {
        Menu target = menuRepository.findById(id)
                .orElseThrow(() -> new ApiException(MENU_NOT_FOUND));

        menuRepository.delete(target);
    }

    public boolean duplicateMenuName(String name, Long id) {
        Menu target = menuRepository.findByName(name);
        if (target != null && target.getMenuGroup().getId() == id) {
            return true;
        }
        return false;
    }

    public void validateMainMenu(Long groupId) {
        mainMenuSizeOver();
        if (groupId != 1) {
            throw new ApiException(MAIN_MENU_NOT_VALIDATED);
        }
    }

    public void mainMenuSizeOver() {
        Long count = menuRepository.countByIsMain(true);
        if (count >= 6) {
            throw new ApiException(MAIN_MENU_SIZE_OVER);
        }
    }

}
