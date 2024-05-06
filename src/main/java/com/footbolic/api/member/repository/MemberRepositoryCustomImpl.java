package com.footbolic.api.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.footbolic.api.member.entity.QMemberEntity.memberEntity;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByIdAtPlatform(String id, String platform) {

        return !queryFactory.selectFrom(memberEntity)
                .where(
                        memberEntity.idAtProvider.eq(id)
                                .and(memberEntity.platform.eq(platform))
                ).fetch().isEmpty();
    }
}
