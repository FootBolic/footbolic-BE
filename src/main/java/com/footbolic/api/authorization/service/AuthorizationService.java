package com.footbolic.api.authorization.service;

import com.footbolic.api.authorization.dto.AuthorizationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorizationService {

    List<AuthorizationDto> findAllAuthorizations(Pageable pageable);

    AuthorizationDto findById(String id);

    AuthorizationDto saveAuthorization(AuthorizationDto role);

    void deleteAuthorization(String id);

    boolean existsById(String id);

}