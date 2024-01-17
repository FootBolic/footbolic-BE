package com.footbolic.api.menu.service;

import com.footbolic.api.menu.dto.MenuDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuService {

    List<MenuDto> findAllMenus();

    MenuDto findById(String id);

    MenuDto saveMenu(MenuDto menu);

    void deleteMenu(String id);

    boolean existsById(String id);

}