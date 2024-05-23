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
    public List<RoleDto> findAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable).stream().map(RoleEntity::toDto).toList();
    }

    @Override
    public RoleDto findById(String id) {
        return roleRepository.findById(id).map(RoleEntity::toDto).orElse(null);
    }

    @Override
    public RoleDto findDefaultRole() {
        return roleRepository.findDefaultRole().toDto();
    }

    @Override
    public RoleDto saveRole(RoleDto role) {
        RoleEntity createdRole = roleRepository.save(role.toEntity());
        return createdRole.toDto();
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
