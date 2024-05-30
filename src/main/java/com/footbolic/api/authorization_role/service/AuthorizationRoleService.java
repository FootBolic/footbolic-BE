package com.footbolic.api.authorization_role.service;

import com.footbolic.api.authorization_role.dto.AuthorizationRoleDto;

public interface AuthorizationRoleService {

    AuthorizationRoleDto saveAuthorizationRole(AuthorizationRoleDto authorizationRole);

    void deleteByRoleAndAuthorization(String roleId, String authorizationId);
}
