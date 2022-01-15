package com.bluedelivery.shop.infra;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.bluedelivery.menu.interfaces.RegisterMenuGroupDto;
import com.bluedelivery.menu.domain.MenuGroup;


@Mapper
@Repository
public interface MenuGroupMapper {

    List<MenuGroup> findMenuGroup(Long shopId);

    List<MenuGroup> findMenuByGroupId(Long groupId);

    int updateMenuGroup(RegisterMenuGroupDto dto);

    int deleteMenuGroup(Long id);

}
