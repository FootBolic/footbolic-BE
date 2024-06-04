package com.footbolic.api.authorization.repository;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.role.entity.RoleEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.authorization.entity.QAuthorizationEntity.authorizationEntity;
import static com.footbolic.api.authorization_role.entity.QAuthorizationRoleEntity.authorizationRoleEntity;
import static com.footbolic.api.role.entity.QRoleEntity.roleEntity;

@Repository
@RequiredArgsConstructor
public class AuthorizationRepositoryCustomImpl implements AuthorizationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AuthorizationEntity> findAll(Pageable pageable, String searchTitle, String searchMenuId) {

        JPAQuery<AuthorizationEntity> query = queryFactory.selectFrom(authorizationEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(authorizationEntity.title.contains(searchTitle));
        }

        if (searchMenuId != null && !searchMenuId.isBlank()) {
            query.where(authorizationEntity.menuId.eq(searchMenuId));
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(authorizationEntity.id.desc())
                .fetch();
    }

    @Override
    public long count(String searchTitle, String searchMenuId) {
        JPAQuery<AuthorizationEntity> query = queryFactory.selectFrom(authorizationEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(authorizationEntity.title.contains(searchTitle));
        }

        if (searchMenuId != null && !searchMenuId.isBlank()) {
            query.where(authorizationEntity.menuId.eq(searchMenuId));
        }

        return query.fetch().size();
    }

    @Override
    public List<AuthorizationEntity> findAllByRoleIds(List<String> roleIds) {
        return queryFactory.selectDistinct(authorizationEntity)
                .from(authorizationEntity)
                .innerJoin(authorizationEntity.authorizationRoles, authorizationRoleEntity)
                .innerJoin(authorizationRoleEntity.role, roleEntity)
                .where(
                        roleEntity.id.in(roleIds)
                )
                .fetch();
    }
}
