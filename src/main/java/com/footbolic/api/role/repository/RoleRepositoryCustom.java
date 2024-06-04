package com.footbolic.api.role.repository;

import com.footbolic.api.role.entity.RoleEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleRepositoryCustom {

    List<RoleEntity> findAll(Pageable pageable, String searchTitle, String searchAuthorizationId);

    List<RoleEntity> findAllByMemberId(String memberId);

    long count(String searchTitle, String searchAuthorizationId);

    List<RoleEntity> findDefaultRoles();

}
