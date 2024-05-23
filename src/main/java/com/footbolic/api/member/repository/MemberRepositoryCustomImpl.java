package com.footbolic.api.member.repository;

import com.footbolic.api.member.entity.MemberEntity;
import com.querydsl.core.types.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.footbolic.api.member.entity.QMemberEntity.memberEntity;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Value("${auth.platform.naver}")
    private String NAVER;

    @Value("${auth.platform.kakao}")
    private String KAKAO;

    @Override
    public List<MemberEntity> findAllActiveMembers(Pageable pageable) {
        String[] platforms = {NAVER, KAKAO};
        return queryFactory.selectFrom(memberEntity)
                .where(
                        memberEntity.platform.in(Arrays.asList(platforms))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(memberEntity.id.desc())
                .fetch();
    }

    @Override
    public long countActiveMembers() {
        String[] platforms = {NAVER, KAKAO};
        return queryFactory.selectFrom(memberEntity)
                .where(
                        memberEntity.platform.in(Arrays.asList(platforms))
                )
                .fetch()
                .size();
    }

    @Override
    public MemberEntity findByIdAtPlatform(String idAtPlatform, String platform) {
        return queryFactory.selectFrom(memberEntity)
                .where(
                        memberEntity.idAtProvider.eq(idAtPlatform)
                                .and(memberEntity.platform.eq(platform))
                ).fetchFirst();
    }

    @Override
    public boolean existsByIdAtPlatform(String idAtPlatform, String platform) {

        return !queryFactory.selectFrom(memberEntity)
                .where(
                        memberEntity.idAtProvider.eq(idAtPlatform)
                                .and(memberEntity.platform.eq(platform))
                ).fetch().isEmpty();
    }

    @Override
    public void updateTokenInfo(MemberEntity member) {
        queryFactory.update(memberEntity)
                .set(memberEntity.refreshToken, member.getRefreshToken())
                .set(memberEntity.refreshTokenExpiresAt, member.getRefreshTokenExpiresAt())
                .set(memberEntity.updatedAt, LocalDateTime.now())
                .where(memberEntity.id.eq(member.getId()))
                .execute();
    }
}
