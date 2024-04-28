package com.footbolic.api.menu.repository;

import com.footbolic.api.menu.entity.MenuEntity;

import java.util.List;

public interface MenuRepositoryCustom {

    List<MenuEntity> findAllMenus();

    List<MenuEntity> findUsedMenus();

}
