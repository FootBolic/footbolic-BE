package com.footbolic.api.member_role.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.footbolic.api.member_role.entity.QMemberRoleEntity.memberRoleEntity;


@Repository
@RequiredArgsConstructor
public class MemberRoleRepositoryCustomImpl implements MemberRoleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteByMemberIdAndRoleId(String memberId, String roleId) {
        queryFactory.delete(memberRoleEntity)
                .where(
                    memberRoleEntity.memberId.eq(memberId)
                            .and(memberRoleEntity.roleId.eq(roleId))
                )
                .execute();
    }

    @Override
    public void deleteAllByMemberId(String memberId) {
        queryFactory.delete(memberRoleEntity)
                .where(memberRoleEntity.memberId.eq(memberId))
                .execute();
    }
}
