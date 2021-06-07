package com.delivery.shop.menu;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MenuGroupMapper {

    int saveMenuGroup(MenuGroupDto dto);

    int groupNameCheck(String name);

}