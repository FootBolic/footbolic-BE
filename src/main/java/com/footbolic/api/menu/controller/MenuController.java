package com.footbolic.api.menu.controller;

import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 API")
@RequestMapping("/menu")
@RequiredArgsConstructor
@RestController
@Slf4j
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "메뉴 생성", description = "파라미터로 전달받은 메뉴를 DB에 생성")
    @Parameter(name = "menu", description = "생성할 메뉴 객체", required = true)
    @PostMapping
    public ResponseEntity<MenuDto> createMenu(
            @RequestBody @Valid MenuDto menu
    ) {
        log.info("here1");
        log.info("title : {}", menu.getTitle());
        log.info("path : {}", menu.getPath());
        log.info("createMemberId : {}", menu.getCreateMemberId());
        MenuDto createdMenu = menuService.createMenu(menu);

        return ResponseEntity.ok(createdMenu);
    }
}
