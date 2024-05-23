package com.footbolic.api.role.repository;

import com.footbolic.api.role.entity.RoleEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.footbolic.api.role.entity.QRoleEntity.roleEntity;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryCustomImpl implements RoleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public RoleEntity findDefaultRole() {
        return queryFactory.selectFrom(roleEntity)
                .where(
                        roleEntity.isDefault.eq(true)
                )
                .orderBy(roleEntity.id.asc())
                .fetchFirst();
    }
}
