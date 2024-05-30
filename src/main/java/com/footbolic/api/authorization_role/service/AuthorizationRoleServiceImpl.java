package com.footbolic.api.authorization_role.service;

import com.footbolic.api.authorization_role.dto.AuthorizationRoleDto;
import com.footbolic.api.authorization_role.repository.AuthorizationRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorizationRoleServiceImpl implements AuthorizationRoleService {

    private final AuthorizationRoleRepository authorizationRoleRepository;

    @Override
    public AuthorizationRoleDto saveAuthorizationRole(AuthorizationRoleDto authorizationRole) {
        return authorizationRoleRepository.save(authorizationRole.toEntity()).toDto();
    }

    @Override
    public void deleteByRoleAndAuthorization(String roleId, String authorizationId) {
        authorizationRoleRepository.deleteByRoleAndAuthorization(roleId, authorizationId);
    }
}
