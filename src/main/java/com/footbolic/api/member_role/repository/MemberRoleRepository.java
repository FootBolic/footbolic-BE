package com.footbolic.api.member_role.repository;

import com.footbolic.api.member_role.entity.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRoleEntity, String>, MemberRoleRepositoryCustom {
}
