package com.footbolic.api.post.repository;

import com.footbolic.api.post.entity.PostEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.post.entity.QPostEntity.postEntity;


@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostEntity> findAll(String boardId, Pageable pageable, String searchTitle) {

        JPAQuery<PostEntity> query = queryFactory.selectFrom(postEntity);

        if (boardId != null && !boardId.isBlank()) {
            query.where(postEntity.boardId.eq(boardId));
        }

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(postEntity.title.contains(searchTitle));
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(postEntity.id.desc())
                .fetch();
    }

    @Override
    public long count(String boardId, String searchTitle) {
        JPAQuery<PostEntity> query = queryFactory.selectFrom(postEntity);

        if (boardId != null && !boardId.isBlank()) {
            query.where(postEntity.boardId.eq(boardId));
        }

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(postEntity.title.contains(searchTitle));
        }

        return query.fetch().size();
    }
}
