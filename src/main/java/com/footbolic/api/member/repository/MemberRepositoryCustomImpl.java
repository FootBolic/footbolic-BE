package com.footbolic.api.member.repository;

import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.member.entity.MemberEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.footbolic.api.member.entity.QMemberEntity.memberEntity;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
