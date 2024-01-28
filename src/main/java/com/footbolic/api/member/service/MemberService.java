package com.footbolic.api.member.service;

import com.footbolic.api.member.dto.MemberDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {

    List<MemberDto> findAllMembers(Pageable pageable);

    MemberDto findById(String id);

    MemberDto saveMember(MemberDto role);

    void deleteMember(String id);

    boolean existsById(String id);

}