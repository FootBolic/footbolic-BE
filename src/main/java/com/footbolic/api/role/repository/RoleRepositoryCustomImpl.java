package com.footbolic.api.role.repository;

import com.footbolic.api.role.entity.RoleEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.authorization_role.entity.QAuthorizationRoleEntity.authorizationRoleEntity;
import static com.footbolic.api.member_role.entity.QMemberRoleEntity.memberRoleEntity;
import static com.footbolic.api.role.entity.QRoleEntity.roleEntity;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryCustomImpl implements RoleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RoleEntity> findAll(Pageable pageable, String searchTitle, String searchAuthorizationId) {
        JPAQuery<RoleEntity> query = queryFactory.selectFrom(roleEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(roleEntity.title.contains(searchTitle));
        }

        if (searchAuthorizationId != null && !searchAuthorizationId.isBlank()) {
            query.innerJoin(roleEntity.authorizationRoles, authorizationRoleEntity)
                    .where(authorizationRoleEntity.authorizationId.eq(searchAuthorizationId));
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(roleEntity.id.desc())
                .fetch();
    }

    @Override
    public List<RoleEntity> findAllByMemberId(String memberId) {
        return queryFactory.selectFrom(roleEntity).leftJoin(roleEntity.memberRoles, memberRoleEntity)
                .where(memberRoleEntity.memberId.eq(memberId)).fetch();
    }

    @Override
    public long count(String searchTitle, String searchAuthorizationId) {
        JPAQuery<RoleEntity> query = queryFactory.selectFrom(roleEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(roleEntity.title.contains(searchTitle));
        }

        if (searchAuthorizationId != null && !searchAuthorizationId.isBlank()) {
            query.innerJoin(roleEntity.authorizationRoles, authorizationRoleEntity)
                    .where(authorizationRoleEntity.authorizationId.eq(searchAuthorizationId));
        }

        return query.fetch().size();
    }

    @Override
    public List<RoleEntity> findDefaultRoles() {
        return queryFactory.selectFrom(roleEntity)
                .where(
                        roleEntity.isDefault.eq(true)
                )
                .orderBy(roleEntity.id.asc())
                .fetch();
    }
}
