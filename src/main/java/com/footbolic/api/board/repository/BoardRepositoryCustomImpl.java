package com.footbolic.api.board.repository;

import com.footbolic.api.board.entity.BoardEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.board.entity.QBoardEntity.boardEntity;


@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardEntity> findAll(Pageable pageable, String searchTitle) {

        JPAQuery<BoardEntity> query = queryFactory.selectFrom(boardEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(boardEntity.title.contains(searchTitle));
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(boardEntity.id.desc())
                .fetch();
    }

    @Override
    public long count(String searchTitle) {
        JPAQuery<BoardEntity> query = queryFactory.selectFrom(boardEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(boardEntity.title.contains(searchTitle));
        }

        return query.fetch().size();
    }

    @Override
    public List<BoardEntity> findMain() {
        return queryFactory.selectFrom(boardEntity)
                .where(boardEntity.isMain.isTrue())
                .fetch();
    }
}
