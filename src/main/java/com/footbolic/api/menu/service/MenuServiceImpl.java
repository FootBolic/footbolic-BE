package com.footbolic.api.menu.service;

import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.menu.entity.MenuEntity;
import com.footbolic.api.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public MenuDto createMenu(MenuDto menu) {
        MenuEntity createdMenu = menuRepository.save(menu.toEntity());
        return createdMenu.toDto();
    }
}
