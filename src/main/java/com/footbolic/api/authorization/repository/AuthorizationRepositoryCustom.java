package com.footbolic.api.authorization.repository;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.role.entity.RoleEntity;

import java.util.List;

public interface AuthorizationRepositoryCustom {

    List<AuthorizationEntity> findAllAuthorizationsByRole(RoleEntity role);
}
