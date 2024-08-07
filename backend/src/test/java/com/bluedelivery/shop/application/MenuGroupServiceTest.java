package com.bluedelivery.shop.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.bluedelivery.common.response.ApiException;
import com.bluedelivery.common.response.ErrorCode;
import com.bluedelivery.menu.domain.MenuGroup;
import com.bluedelivery.menu.domain.MenuGroupRepository;
import com.bluedelivery.menu.interfaces.RegisterMenuGroupDto;
import com.bluedelivery.menu.interfaces.UpdateMenuGroupDto;
import com.bluedelivery.shop.application.adapter.MenuGroupServiceImpl;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MenuGroupServiceTest {

    @InjectMocks
    MenuGroupServiceImpl service;

    @Mock
    MenuGroupRepository repository;

    @Mock
    MenuGroup menuGroup;

    @Test
    @DisplayName("메뉴 그룹 생성 테스트")
    public void registerMenuGroupTest() {
        RegisterMenuGroupDto dto = new RegisterMenuGroupDto();
        dto.setShopId(1L);
        dto.setName("사이드메뉴");
        dto.setContent("5000원");

        given(service.registerMenuGroup(dto)).willReturn(dto.toEntity());

        service.registerMenuGroup(dto);

        verify(repository, times(1)).save(dto.toEntity());
    }

    @Test
    @DisplayName("메뉴 그룹 이름 중복 예외 테스트")
    public void menuGroupNameDuplicateTest() {
        RegisterMenuGroupDto dto = new RegisterMenuGroupDto();
        dto.setShopId(1L);
        dto.setName("사이드메뉴");
        dto.setContent("5000원");

        given(repository.save(dto.toEntity())).willThrow(new ApiException(ErrorCode.GROUP_ALREADY_EXISTS));

        assertThrows(ApiException.class, () -> service.registerMenuGroup(dto)).getError();

    }

    @Test
    @DisplayName("메뉴 그룹 수정 테스트")
    public void updateMenuGroupTest() {
        UpdateMenuGroupDto dto = new UpdateMenuGroupDto();
        dto.setId(1L);
        dto.setShopId(1L);
        dto.setName("사이드메뉴");
        dto.setContent("5000원");

        given(repository.findById(dto.getId())).willReturn(Optional.of(menuGroup));

        service.updateMenuGroup(dto);

        verify(menuGroup, times(1)).setName(dto.getName());
        verify(menuGroup, times(1)).setContent(dto.getContent());
    }

    @Test
    @DisplayName("메뉴 그룹 삭제 테스트")
    public void deleteMenuGroupTest() {
        Long id = 1L;

        MenuGroup getMenuGroup = new MenuGroup();

        given(repository.findById(id)).willReturn(Optional.of(getMenuGroup));

        service.deleteMenuGroup(id);

        verify(repository, times(1)).delete(getMenuGroup);
    }

}
