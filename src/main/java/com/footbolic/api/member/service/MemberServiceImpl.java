package com.footbolic.api.member.service;

import com.footbolic.api.member.repository.MemberRepository;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<MemberDto> findAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable).stream().map(MemberEntity::toDto).toList();
    }

    @Override
    public MemberDto findById(String id) {
        return memberRepository.findById(id).map(MemberEntity::toDto).orElse(null);
    }

    @Override
    public MemberDto saveMember(MemberDto member) {
        MemberEntity createdMember = memberRepository.save(member.toEntity());
        return createdMember.toDto();
    }

    @Override
    public void deleteMember(String id) {
        memberRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return memberRepository.existsById(id);
    }

    @Override
    public boolean existsByIdAtPlatform(String id, String platform) {
        return memberRepository.existsByIdAtPlatform(id, platform);
    }

}
