package com.footbolic.api.authorization_role.service;

import com.footbolic.api.authorization_role.dto.AuthorizationRoleDto;

public interface AuthorizationRoleService {

    AuthorizationRoleDto saveAuthorizationRole(AuthorizationRoleDto authorizationRole);

    void deleteByAuthorizationIdAndRoleId(String authorizationId, String roleId);
}
