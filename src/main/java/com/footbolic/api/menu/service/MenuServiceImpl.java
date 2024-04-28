package com.footbolic.api.menu.service;

import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.menu.entity.MenuEntity;
import com.footbolic.api.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public List<MenuDto> findAllMenus() {
        return menuRepository.findAllMenus().stream().map(MenuEntity::toDto).toList();
    }

    @Override
    public List<MenuDto> findUsedMenus() {
        return menuRepository.findUsedMenus().stream().map(MenuEntity::toDto).toList();
    }

    @Override
    public MenuDto findById(String id) {
        return menuRepository.findById(id).map(MenuEntity::toDto).orElse(null);
    }

    @Override
    public MenuDto saveMenu(MenuDto menu) {
        MenuEntity createdMenu = menuRepository.save(menu.toEntity());
        return createdMenu.toDto();
    }

    @Override
    public void deleteMenu(String id) {
        menuRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return menuRepository.existsById(id);
    }

}
