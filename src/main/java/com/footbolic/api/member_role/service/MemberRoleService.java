package com.footbolic.api.member_role.service;

import com.footbolic.api.member_role.dto.MemberRoleDto;

public interface MemberRoleService {

    MemberRoleDto saveMemberRole(MemberRoleDto memberRole);

    void deleteByMemberIdAndRoleId(String MemberId, String roleId);

    void deleteAllByMemberId(String memberId);
}
