package com.footbolic.api.menu.repository;

import com.footbolic.api.menu.entity.MenuEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.menu.entity.QMenuEntity.menuEntity;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenuEntity> findAllMenus() {
        return queryFactory.selectFrom(menuEntity).where(menuEntity.parentId.isNull()).fetch();
    }

}
