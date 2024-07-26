package com.footbolic.api.member.service;

import com.footbolic.api.member.repository.MemberRepository;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.member.entity.MemberEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<MemberDto> findAll(Pageable pageable, String searchNickname, String searchPlatform, String searchRoleId) {
        return memberRepository.findAllActiveMembers(pageable, searchNickname, searchPlatform, searchRoleId)
                .stream().map(MemberEntity::toDto).toList();
    }

    @Override
    public long count(String searchNickname, String searchPlatform, String searchRoleId) {
        return memberRepository.countActiveMembers(searchNickname, searchPlatform, searchRoleId);
    }

    @Override
    public MemberDto findById(String id) {
        return memberRepository.findById(id).map(MemberEntity::toDto).orElse(null);
    }

    @Override
    public MemberDto findByIdAtPlatform(String idAtPlatform, String platform) {
        return memberRepository.findByIdAtPlatform(idAtPlatform, platform).toDto();
    }

    @Override
    public MemberDto saveMember(MemberDto member) {
        return memberRepository.save(member.toEntity()).toDto();
    }

    @Override
    public void deleteMember(String id) {
        memberRepository.deleteById(id);
    }

    @Override
    public void withdraw(MemberDto member) {
        member.setNickname("-");
        member.setPlatform("-");
        member.setIdAtProvider("-");
        member.setRefreshToken(null);
        member.setRefreshTokenExpiresAt(null);
        member.setNicknameLastUpdatedAt(null);
        member.setUpdatedAt(LocalDateTime.now());
        memberRepository.save(member.toEntity());
    }

    @Override
    public boolean existsById(String id) {
        return memberRepository.existsById(id);
    }

    @Override
    public boolean existsByIdAtPlatform(String idAtPlatform, String platform) {
        return memberRepository.existsByIdAtPlatform(idAtPlatform, platform);
    }

    @Override
    public void updateTokenInfo(MemberDto member) {
        memberRepository.updateTokenInfo(member.toEntity());
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

}
