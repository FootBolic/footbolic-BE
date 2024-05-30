package com.footbolic.api.authorization_role.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.footbolic.api.authorization_role.entity.QAuthorizationRoleEntity.authorizationRoleEntity;

@Repository
@RequiredArgsConstructor
public class AuthorizationRoleRepositoryCustomImpl implements AuthorizationRoleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteByRoleAndAuthorization(String roleId, String authorizationId) {
        queryFactory.delete(authorizationRoleEntity).where(
                authorizationRoleEntity.roleId.eq(roleId)
                        .and(authorizationRoleEntity.authorizationId.eq(authorizationId))
        ).execute();
    }
}
