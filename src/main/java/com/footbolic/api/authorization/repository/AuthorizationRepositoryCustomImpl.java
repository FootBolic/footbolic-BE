package com.footbolic.api.authorization.repository;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.role.entity.RoleEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.authorization.entity.QAuthorizationEntity.authorizationEntity;

@Repository
@RequiredArgsConstructor
public class AuthorizationRepositoryCustomImpl implements AuthorizationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AuthorizationEntity> findAllAuthorizationsByRole(RoleEntity role) {
        return queryFactory.selectFrom(authorizationEntity)
                .where(authorizationEntity.roleId.eq(role.getId())).fetch();
    }
}
