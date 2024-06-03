package com.footbolic.api.role.service;

import com.footbolic.api.authorization_role.dto.AuthorizationRoleDto;
import com.footbolic.api.role.dto.RoleDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {

    List<RoleDto> findAll();

    List<RoleDto> findAll(Pageable pageable, String searchTitle, String searchAuthorizationId);

    List<RoleDto> findAllByMemberId(String memberId);

    long count(String searchTitle, String searchAuthorizationId);

    RoleDto findById(String id);

    List<RoleDto> findDefaultRoles();

    RoleDto saveRole(RoleDto role);

    void deleteRole(String id);

    boolean existsById(String id);

}