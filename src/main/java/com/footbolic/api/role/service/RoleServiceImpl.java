package com.footbolic.api.role.service;

import com.footbolic.api.role.dto.RoleDto;
import com.footbolic.api.role.entity.RoleEntity;
import com.footbolic.api.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleDto> findAll() {
        return roleRepository.findAll().stream().map(RoleEntity::toDto).toList();
    }

    @Override
    public List<RoleDto> findAll(Pageable pageable, String searchTitle, String searchAuthorizationId) {
        return roleRepository.findAll(pageable, searchTitle, searchAuthorizationId)
                .stream().map(RoleEntity::toDto).toList();
    }

    @Override
    public List<RoleDto> findAllByMemberId(String memberId) {
        return roleRepository.findAllByMemberId(memberId).stream().map(RoleEntity::toDto).toList();
    }

    @Override
    public long count(String searchTitle, String searchAuthorizationId) {
        return roleRepository.count(searchTitle, searchAuthorizationId);
    }

    @Override
    public RoleDto findById(String id) {
        return roleRepository.findById(id).map(RoleEntity::toDto).orElse(null);
    }

    @Override
    public List<RoleDto> findDefaultRoles() {
        return roleRepository.findDefaultRoles().stream().map(RoleEntity::toDto).toList();
    }

    @Override
    public RoleDto saveRole(RoleDto role) {
        return roleRepository.save(role.toEntity()).toDto();
    }

    @Override
    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return roleRepository.existsById(id);
    }

}
