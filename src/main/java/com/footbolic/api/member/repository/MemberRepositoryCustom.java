package com.footbolic.api.member.repository;

import com.footbolic.api.member.entity.MemberEntity;

public interface MemberRepositoryCustom {

    MemberEntity findByIdAtPlatform(String idAtPlatform, String platform);

    boolean existsByIdAtPlatform(String idAtPlatform, String platform);

    void updateTokenInfo(MemberEntity member);

}
