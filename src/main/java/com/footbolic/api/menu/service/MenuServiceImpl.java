package com.footbolic.api.menu.service;

import com.footbolic.api.authorization.dto.AuthorizationDto;
import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.menu.entity.MenuEntity;
import com.footbolic.api.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public List<MenuDto> findAll() {
        return menuRepository.findAll().stream().map(MenuEntity::toDto).toList();
    }

    @Override
    public List<MenuDto> findAllByAuthorizations(List<AuthorizationDto> authorizations) {
        List<MenuDto> allMenus = findAll();
        List<String> menuIds = authorizations.stream().map(AuthorizationDto::getMenuId).toList();
        return filterByAuth(allMenus, menuIds);
    }

    private List<MenuDto> filterByAuth(List<MenuDto> menus, List<String> targets) {
        return menus.stream()
                .filter(MenuDto::isUsed)
                .filter(menu -> targets.contains(menu.getId()))
                .peek(menu -> {
                    if (!menu.getChildren().isEmpty()) {
                        menu.setChildren(filterByAuth(menu.getChildren(), targets));
                    }
                })
                .toList();
    }

    @Override
    public MenuDto findById(String id) {
        return menuRepository.findById(id).map(MenuEntity::toDto).orElse(null);
    }

    @Override
    public MenuDto findPath(String id) {
        MenuDto menu = menuRepository.findById(id).map(MenuEntity::toPublicDto).orElse(null);

        if (menu != null && menu.isUsed()) {
            if (menu.getParentId() != null && !menu.getParentId().isBlank()) {
                MenuDto parent = findPath(menu.getParentId());

                if (parent == null || parent.getId().isBlank()) {
                    return null;
                } else {
                    menu.setParent(parent);
                }
            }

            return menu;
        }

        return null;
    }

    @Override
    public MenuDto saveMenu(MenuDto menu) {
        MenuEntity createdMenu = menuRepository.save(menu.toEntity());
        return createdMenu.toDto();
    }

    @Override
    public void deleteMenu(String id) {
        try {
            menuRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    @Override
    public boolean existsById(String id) {
        return menuRepository.existsById(id);
    }

}
