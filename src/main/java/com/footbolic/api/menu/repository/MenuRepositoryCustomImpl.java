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
    public List<MenuEntity> findAll() {
        return queryFactory
                .selectFrom(menuEntity)
                .where(menuEntity.parentId.isNull())
                .orderBy(menuEntity.order.asc(), menuEntity.id.desc())
                .fetch();
    }

    @Override
    public MenuEntity findByBoardId(String boardId) {
        return queryFactory.selectFrom(menuEntity)
                .where(
                        menuEntity.isUsed.isTrue()
                        .and(
                                menuEntity.program.code.eq("PROGRAM_BOARD")
                        )
                        .and(
                                menuEntity.detailId.eq(boardId)
                        )
                )
                .fetchFirst();
    }

}
