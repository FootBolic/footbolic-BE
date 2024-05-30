package com.footbolic.api.authorization_role.repository;

import com.footbolic.api.member.entity.MemberEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorizationRoleRepositoryCustom {
    void deleteByRoleAndAuthorization(String roleId, String authorizationId);
}
