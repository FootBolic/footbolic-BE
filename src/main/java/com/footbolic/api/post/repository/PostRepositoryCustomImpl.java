package com.footbolic.api.post.repository;

import com.footbolic.api.post.entity.PostEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.footbolic.api.member.entity.QMemberEntity.memberEntity;
import static com.footbolic.api.post.entity.QPostEntity.postEntity;


@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostEntity> findAll(String boardId, Pageable pageable, String searchTitle, String searchCreatedBy, LocalDateTime searchCreatedAt) {

        JPAQuery<PostEntity> query = queryFactory.selectFrom(postEntity);

        if (boardId != null && !boardId.isBlank()) {
            query.where(postEntity.boardId.eq(boardId));
        }

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(postEntity.title.contains(searchTitle));
        }

        if (searchCreatedBy != null && !searchCreatedBy.isBlank()) {
            query.innerJoin(memberEntity)
                            .on(postEntity.createMemberId.eq(memberEntity.id)
                                    .and(memberEntity.nickname.contains(searchCreatedBy))
                            );
        }

        if (searchCreatedAt != null) {
            LocalDateTime date = searchCreatedAt.withHour(0).withMinute(0).withSecond(0).withNano(0);
            query.where(postEntity.createdAt.between(date, date.plusDays(1)));
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(postEntity.id.desc())
                .fetch();
    }

    @Override
    public long count(String boardId, String searchTitle, String searchCreatedBy, LocalDateTime searchCreatedAt) {
        JPAQuery<PostEntity> query = queryFactory.selectFrom(postEntity);

        if (boardId != null && !boardId.isBlank()) {
            query.where(postEntity.boardId.eq(boardId));
        }

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(postEntity.title.contains(searchTitle));
        }

        if (searchCreatedBy != null && !searchCreatedBy.isBlank()) {
            query.innerJoin(memberEntity)
                    .on(postEntity.createMemberId.eq(memberEntity.id)
                            .and(memberEntity.nickname.contains(searchCreatedBy))
                    );
        }

        if (searchCreatedAt != null) {
            LocalDateTime date = searchCreatedAt.withHour(0).withMinute(0).withSecond(0).withNano(0);
            query.where(postEntity.createdAt.between(date, date.plusDays(1)));
        }

        return query.fetch().size();
    }
}
