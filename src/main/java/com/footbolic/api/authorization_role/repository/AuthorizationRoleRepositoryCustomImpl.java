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
    public void deleteByAuthorizationIdAndRoleId(String authorizationId, String roleId) {
        queryFactory.delete(authorizationRoleEntity)
                .where(
                    authorizationRoleEntity.authorizationId.eq(authorizationId)
                            .and(authorizationRoleEntity.roleId.eq(roleId))
                )
                .execute();
    }
}
