package com.footbolic.api.authorization.service;

import com.footbolic.api.authorization.repository.AuthorizationRepository;
import com.footbolic.api.authorization.dto.AuthorizationDto;
import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.role.dto.RoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final AuthorizationRepository authorizationRepository;

    @Override
    public List<AuthorizationDto> findAll() {
        return authorizationRepository.findAll().stream().map(AuthorizationEntity::toDto).toList();
    }

    @Override
    public List<AuthorizationDto> findAll(Pageable pageable, String searchTitle, String searchMenuId) {
        return authorizationRepository.findAll(pageable, searchTitle, searchMenuId)
                .stream()
                .map(AuthorizationEntity::toDto)
                .toList();
    }

    @Override
    public long count(String searchTitle, String searchMenuId) {
        return authorizationRepository.count(searchTitle, searchMenuId);
    }

    @Override
    public List<AuthorizationDto> findAllAuthorizationsByRole(RoleDto role) {
        return authorizationRepository.findAllAuthorizationsByRole(role.toEntity())
                .stream().map(AuthorizationEntity::toDto).toList();
    }


    @Override
    public AuthorizationDto findById(String id) {
        return authorizationRepository.findById(id).map(AuthorizationEntity::toDto).orElse(null);
    }

    @Override
    public AuthorizationDto saveAuthorization(AuthorizationDto role) {
        AuthorizationEntity createdAuthorization = authorizationRepository.save(role.toEntity());
        return createdAuthorization.toDto();
    }

    @Override
    public void deleteAuthorization(String id) {
        authorizationRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return authorizationRepository.existsById(id);
    }

}
