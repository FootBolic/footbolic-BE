package com.footbolic.api.member.repository;

import com.footbolic.api.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String>, MemberRepositoryCustom {
}
