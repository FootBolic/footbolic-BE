package com.footbolic.api.authorization_role.repository;

import com.footbolic.api.authorization_role.entity.AuthorizationRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRoleRepository extends JpaRepository<AuthorizationRoleEntity, String>, AuthorizationRoleRepositoryCustom {
}
