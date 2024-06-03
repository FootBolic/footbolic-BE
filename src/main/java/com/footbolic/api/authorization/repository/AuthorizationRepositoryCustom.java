package com.footbolic.api.authorization.repository;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorizationRepositoryCustom {

    List<AuthorizationEntity> findAll(Pageable pageable, String searchTitle, String searchMenuId);

    long count(String searchTitle, String searchMenuId);

    List<AuthorizationEntity> findAllByRoleIds(List<String> roleIds);

}
