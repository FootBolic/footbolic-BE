package com.footbolic.api.member_role.service;

import com.footbolic.api.member_role.dto.MemberRoleDto;
import com.footbolic.api.member_role.repository.MemberRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberRoleServiceImpl implements MemberRoleService {

    private final MemberRoleRepository memberRoleRepository;

    @Override
    public MemberRoleDto saveMemberRole(MemberRoleDto memberRole) {
        return memberRoleRepository.save(memberRole.toEntity()).toDto();
    }

    @Override
    public void deleteByMemberIdAndRoleId(String MemberId, String roleId) {
        memberRoleRepository.deleteByMemberIdAndRoleId(MemberId, roleId);
    }

    @Override
    public void deleteAllByMemberId(String memberId) {
        memberRoleRepository.deleteAllByMemberId(memberId);
    }
}
