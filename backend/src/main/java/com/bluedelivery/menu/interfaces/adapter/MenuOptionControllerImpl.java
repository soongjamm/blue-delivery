package com.bluedelivery.menu.interfaces.adapter;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bluedelivery.common.response.HttpResponse;
import com.bluedelivery.menu.interfaces.MenuOptionController;
import com.bluedelivery.menu.interfaces.MenuOptionDto;
import com.bluedelivery.menu.interfaces.MenuOptionGroupDto;
import com.bluedelivery.shop.application.MenuOptionService;
import com.bluedelivery.shop.application.MenuOptionServiceImpl;

@RestController
public class MenuOptionControllerImpl implements MenuOptionController {

    private MenuOptionService service;

    public MenuOptionControllerImpl(MenuOptionServiceImpl service) {
        this.service = service;
    }

    /**
     * 메뉴 옵션 그룹 추가
     *
     * @param menuId 옵션 그룹을 생성할 메뉴의 Id
     * @param dto 생성할 옵션 그룹
     *
     */
    @Override
    @PostMapping("/{menuId}/option-groups")
    public ResponseEntity<HttpResponse<?>> registerMenuOptionGroup(@PathVariable Long menuId,
                                                                   @RequestBody @Valid MenuOptionGroupDto dto) {
        dto.setMenuId(menuId);
        service.registerMenuOptionGroup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(HttpResponse.response(dto));
    }


    /**
     * 메뉴 옵션 추가
     *
     * @param optionGroupId 메뉴 옵션을 생성할 메뉴 옵션 그룹 Id
     * @param dto 생성할 옵션
     *
     */
    @Override
    @PostMapping("/{optionGroupId}/options")
    public ResponseEntity<HttpResponse<?>> registerMenuOption(@PathVariable Long optionGroupId,
                                                              @RequestBody MenuOptionDto dto) {
        dto.setOptionGroupId(optionGroupId);
        service.registerMenuOption(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(HttpResponse.response(dto));
    }

}
