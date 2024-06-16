package com.footbolic.api.image.repository;

import com.footbolic.api.image.entity.ImageEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.image.entity.QImageEntity.imageEntity;


@Repository
@RequiredArgsConstructor
public class ImageRepositoryCustomImpl implements ImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ImageEntity> findAll(String commentId) {
        JPAQuery<ImageEntity> query = queryFactory.selectFrom(imageEntity);

        if (commentId != null && !commentId.isBlank()) {
//            query.where(imageEntity.commentId.eq(commentId));
        }

        return query.orderBy(imageEntity.id.asc()).fetch();
    }

    @Override
    public long count(String commentId) {
        JPAQuery<ImageEntity> query = queryFactory.selectFrom(imageEntity);

        if (commentId != null && !commentId.isBlank()) {
//            query.where(imageEntity.commentId.eq(commentId));
        }

        return query.fetch().size();
    }
}
