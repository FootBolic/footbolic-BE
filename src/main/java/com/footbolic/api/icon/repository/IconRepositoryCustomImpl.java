package com.footbolic.api.icon.repository;

import com.footbolic.api.icon.entity.IconEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.icon.entity.QIconEntity.iconEntity;


@Repository
@RequiredArgsConstructor
public class IconRepositoryCustomImpl implements IconRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<IconEntity> findAll(Pageable pageable, String searchTitle, String searchCode) {

        JPAQuery<IconEntity> query = queryFactory.selectFrom(iconEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(iconEntity.title.contains(searchTitle));
        }

        if (searchCode != null && !searchCode.isBlank()) {
            query.where(iconEntity.code.contains(searchCode));
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(iconEntity.id.desc())
                .fetch();
    }

    @Override
    public long count(String searchTitle, String searchCode) {
        JPAQuery<IconEntity> query = queryFactory.selectFrom(iconEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(iconEntity.title.contains(searchTitle));
        }

        if (searchCode != null && !searchCode.isBlank()) {
            query.where(iconEntity.code.contains(searchCode));
        }

        return query.fetch().size();
    }
}
