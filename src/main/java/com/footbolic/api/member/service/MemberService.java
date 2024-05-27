package com.footbolic.api.member.service;

import com.footbolic.api.member.dto.MemberDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {

    List<MemberDto> findAll(Pageable pageable, String searchNickname, String searchPlatform, String searchRoleId);

    long count(String searchNickname, String searchPlatform, String searchRoleId);

    MemberDto findById(String id);

    MemberDto findByIdAtPlatform(String idAtPlatform, String platform);

    MemberDto saveMember(MemberDto member);

    void deleteMember(String id);

    void withdraw(MemberDto member);

    boolean existsById(String id);

    boolean existsByIdAtPlatform(String idAtPlatform, String platform);

    void updateTokenInfo(MemberDto member);

}