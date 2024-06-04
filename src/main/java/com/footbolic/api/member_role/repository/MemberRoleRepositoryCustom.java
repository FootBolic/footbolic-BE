package com.footbolic.api.member_role.repository;

public interface MemberRoleRepositoryCustom {

    void deleteByMemberIdAndRoleId(String memberId, String roleId);

    void deleteAllByMemberId(String memberId);

}
