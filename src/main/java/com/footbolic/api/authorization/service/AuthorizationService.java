package com.footbolic.api.authorization.service;

import com.footbolic.api.authorization.dto.AuthorizationDto;
import com.footbolic.api.role.dto.RoleDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorizationService {

    List<AuthorizationDto> findAllAuthorizations(Pageable pageable);

    List<AuthorizationDto> findAllAuthorizationsByRole(RoleDto role);

    AuthorizationDto findById(String id);

    AuthorizationDto saveAuthorization(AuthorizationDto role);

    void deleteAuthorization(String id);

    boolean existsById(String id);

}