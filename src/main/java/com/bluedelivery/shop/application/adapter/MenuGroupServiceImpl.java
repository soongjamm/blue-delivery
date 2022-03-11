package com.bluedelivery.shop.application.adapter;

import static com.bluedelivery.common.response.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluedelivery.common.response.ApiException;
import com.bluedelivery.menu.domain.MenuGroup;
import com.bluedelivery.menu.domain.MenuGroupRepository;
import com.bluedelivery.menu.interfaces.RegisterMenuGroupDto;
import com.bluedelivery.menu.interfaces.UpdateMenuGroupDto;
import com.bluedelivery.shop.application.MenuGroupService;


@Service
public class MenuGroupServiceImpl implements MenuGroupService {

    private final MenuGroupRepository repository;

    public MenuGroupServiceImpl(MenuGroupRepository repository) {
        this.repository = repository;
    }

    public MenuGroup registerMenuGroup(RegisterMenuGroupDto dto) {
        if (duplicateGroupName(dto.getName())) {
            throw new ApiException(GROUP_ALREADY_EXISTS);
        }
        return repository.save(dto.toEntity());
    }

    @Transactional
    public void updateMenuGroup(UpdateMenuGroupDto dto) {
        MenuGroup target = repository.findById(dto.getId()).orElseThrow(() -> new ApiException(MENU_GROUP_NOT_FOUND));

        target.setName(dto.getName());
        target.setContent(dto.getContent());
    }

    public void deleteMenuGroup(Long id) {
        MenuGroup target = repository.findById(id).orElseThrow(() -> new ApiException(MENU_GROUP_NOT_FOUND));
        repository.delete(target);
    }

    public boolean duplicateGroupName(String name) {
        MenuGroup findName = repository.findByName(name);
        if (findName != null) {
            return true;
        }
        return false;
    }

}
