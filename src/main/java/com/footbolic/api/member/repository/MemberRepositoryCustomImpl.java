package com.footbolic.api.member.repository;

import com.footbolic.api.member.entity.MemberEntity;
import com.querydsl.core.types.Order;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.footbolic.api.member.entity.QMemberEntity.memberEntity;
import static com.footbolic.api.member_role.entity.QMemberRoleEntity.memberRoleEntity;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Value("${auth.platform.naver}")
    private String NAVER;

    @Value("${auth.platform.kakao}")
    private String KAKAO;

    @Override
    public List<MemberEntity> findAllActiveMembers(Pageable pageable, String searchNickname, String searchPlatform, String searchRoleId) {
        JPAQuery<MemberEntity> query = queryFactory.selectFrom(memberEntity);

        if (searchNickname != null && !searchNickname.isBlank()) {
            query.where(memberEntity.nickname.contains(searchNickname));
        }

        if (searchRoleId != null && !searchRoleId.isBlank()) {
            query.innerJoin(memberEntity.memberRoles, memberRoleEntity)
                    .where(memberRoleEntity.roleId.eq(searchRoleId));
        }

        if (searchPlatform != null && !searchPlatform.isBlank()) {
            query.where(memberEntity.platform.eq(searchPlatform));
        } else {
            String[] platforms = {NAVER, KAKAO};
            query.where(memberEntity.platform.in(Arrays.asList(platforms)));
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(memberEntity.id.desc())
                .fetch();
    }

    @Override
    public long countActiveMembers(String searchNickname, String searchPlatform, String searchRoleId) {
        JPAQuery<MemberEntity> query = queryFactory.selectFrom(memberEntity);

        if (searchNickname != null && !searchNickname.isBlank()) {
            query.where(memberEntity.nickname.contains(searchNickname));
        }

        if (searchRoleId != null && !searchRoleId.isBlank()) {
            query.innerJoin(memberEntity.memberRoles, memberRoleEntity)
                    .where(memberRoleEntity.roleId.eq(searchRoleId));
        }

        if (searchPlatform != null && !searchPlatform.isBlank()) {
            query.where(memberEntity.platform.eq(searchPlatform));
        } else {
            String[] platforms = {NAVER, KAKAO};
            query.where(memberEntity.platform.in(Arrays.asList(platforms)));
        }

        return query.fetch().size();
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
                .where(memberEntity.id.eq(member.getId()))
                .execute();
    }
}
