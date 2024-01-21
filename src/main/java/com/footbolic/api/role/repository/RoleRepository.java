package com.footbolic.api.role.repository;

import com.footbolic.api.role.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String>, RoleRepositoryCustom {
}
