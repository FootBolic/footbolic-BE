package com.footbolic.api.role.service;

import com.footbolic.api.role.dto.RoleDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {

    List<RoleDto> findAllRoles(Pageable pageable);

    RoleDto findById(String id);

    RoleDto saveRole(RoleDto role);

    void deleteRole(String id);

    boolean existsById(String id);

}