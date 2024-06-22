package com.footbolic.api.banner.repository;

import com.footbolic.api.banner.entity.BannerEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.footbolic.api.banner.entity.QBannerEntity.bannerEntity;

@Repository
@RequiredArgsConstructor
public class BannerRepositoryCustomImpl implements BannerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BannerEntity> findAll(Pageable pageable, String searchTitle, LocalDateTime searchDate) {

        JPAQuery<BannerEntity> query = queryFactory.selectFrom(bannerEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(bannerEntity.title.contains(searchTitle));
        }

        if (searchDate != null && bannerEntity.startsAt != null && bannerEntity.endsAt != null) {
            LocalDateTime date = searchDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
            query.where(bannerEntity.endsAt.after(date).and(bannerEntity.startsAt.before(date.plusDays(1))));
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bannerEntity.id.desc())
                .fetch();
    }

    @Override
    public long count(String searchTitle, LocalDateTime searchDate) {
        JPAQuery<BannerEntity> query = queryFactory.selectFrom(bannerEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(bannerEntity.title.contains(searchTitle));
        }

        if (searchDate != null && bannerEntity.startsAt != null && bannerEntity.endsAt != null) {
            LocalDateTime date = searchDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
            query.where(bannerEntity.endsAt.after(date).and(bannerEntity.startsAt.before(date.plusDays(1))));
        }

        return query.fetch().size();
    }
}
