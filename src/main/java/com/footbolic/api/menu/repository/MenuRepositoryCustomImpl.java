package com.footbolic.api.menu.repository;

import com.footbolic.api.menu.entity.MenuEntity;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.menu.entity.QMenuEntity.menuEntity;
import static com.footbolic.api.program.entity.QProgramEntity.programEntity;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenuEntity> findAll() {
        return queryFactory
                .selectFrom(menuEntity)
                .where(menuEntity.parentId.isNull())
                .orderBy(menuEntity.order.asc(), menuEntity.id.desc())
                .fetch();
    }

    @Override
    public MenuEntity findMenuPathByProgramIdAndDetailId(String programId, String detailId, String menuId) {
        JPAQuery<MenuEntity> query = queryFactory.selectFrom(menuEntity)
                .where(menuEntity.programId.eq(programId));

        if (detailId != null && !detailId.isBlank()) {
            query.where(menuEntity.detailId.eq(detailId));
        }

        if (menuId != null && !menuId.isBlank()) {
            query.where(menuEntity.id.ne(menuId));
        }

        return query.fetchFirst();
    }

    @Override
    public MenuEntity findByPath(String path) {
        List<MenuEntity> menus = queryFactory.selectFrom(menuEntity)
                .leftJoin(menuEntity.program, programEntity)
                .where(
                        Expressions.stringTemplate("{0}", path)
                                .like(Expressions.stringTemplate("concat({0} ,'%')", programEntity.path))
                )
                .fetch();

        if (menus.size() > 1) {
            menus = menus.stream().filter(e -> path.contains(e.getDetailId())).toList();
        }

        if (menus.isEmpty()) {
            return null;
        } else {
            return menus.get(0);
        }
    }

}
