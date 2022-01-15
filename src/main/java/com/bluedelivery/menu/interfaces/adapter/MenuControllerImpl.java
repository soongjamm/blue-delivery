package com.bluedelivery.menu.interfaces.adapter;

import static com.bluedelivery.common.response.HttpResponse.*;

import javax.validation.Valid;

import com.bluedelivery.menu.interfaces.MenuController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluedelivery.menu.interfaces.RegisterMenuDto;
import com.bluedelivery.menu.interfaces.UpdateMenuDto;
import com.bluedelivery.shop.application.MenuService;
import com.bluedelivery.common.response.HttpResponse;

@RestController
@RequestMapping("/menu-groups/{menuGroupId}")
public class MenuControllerImpl implements MenuController {

    private final MenuService service;

    public MenuControllerImpl(MenuService service) {
        this.service = service;
    }


    @PostMapping("/menus")
    public ResponseEntity<HttpResponse<?>> registerMenu(@PathVariable Long menuGroupId,
                                                        @RequestBody @Valid RegisterMenuDto dto) {
        dto.setMenuGroupId(menuGroupId);
        service.registerMenu(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response(dto));
    }


    @PatchMapping("/menus/main-set/{menuId}")
    public ResponseEntity<HttpResponse<?>> setMainMenu(@PathVariable Long menuGroupId,
                                                            @PathVariable Long menuId) {
        service.setMainMenu(menuId);
        return ResponseEntity.status(HttpStatus.OK).body(response(SUCCESS, menuId));
    }


    // TODO Dto 제거 및 (이름, 가격), (구성, 설명), (상태)로 API 분리
    @PatchMapping("/menus/{menuId}")
    public ResponseEntity<HttpResponse<?>> updateMenuStatus(@PathVariable Long menuGroupId,
                                                          @PathVariable Long menuId,
                                                          @RequestBody @Valid UpdateMenuDto dto) {
        service.updateMenuStatus(menuId, dto.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(response(dto));
    }

    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<HttpResponse<?>> deleteMenu(@PathVariable Long menuGroupId,
                                                   @PathVariable Long menuId) {
        service.deleteMenu(menuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response(menuId));
    }

}
