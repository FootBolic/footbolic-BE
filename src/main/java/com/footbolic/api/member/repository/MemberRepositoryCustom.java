package com.footbolic.api.member.repository;

import com.footbolic.api.member.entity.MemberEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberEntity> findAllActiveMembers(Pageable pageable);

    long countActiveMembers();

    MemberEntity findByIdAtPlatform(String idAtPlatform, String platform);

    boolean existsByIdAtPlatform(String idAtPlatform, String platform);

    void updateTokenInfo(MemberEntity member);

}
