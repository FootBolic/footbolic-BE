package com.footbolic.api.menu.service;

import com.footbolic.api.authorization.dto.AuthorizationDto;
import com.footbolic.api.menu.dto.MenuDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuService {

    List<MenuDto> findAll();

    List<MenuDto> findAllByAuthorizations(List<AuthorizationDto> authorizations);

    MenuDto findById(String id);

    MenuDto findMenuPathByProgramIdAndDetailId(String programId, String detailId, String menuId);

    MenuDto findByPath(String path);

    MenuDto saveMenu(MenuDto menu);

    void deleteMenu(String id);

    boolean existsById(String id);

}