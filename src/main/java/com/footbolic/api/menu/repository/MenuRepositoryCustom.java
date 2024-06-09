package com.footbolic.api.menu.repository;

import com.footbolic.api.menu.entity.MenuEntity;

import java.util.List;

public interface MenuRepositoryCustom {

    List<MenuEntity> findAll();

    MenuEntity findMenuPathByProgramIdAndDetailId(String programId, String detailId, String menuId);

    MenuEntity findByPath(String path);
}
