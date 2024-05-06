package com.footbolic.api.member.repository;

public interface MemberRepositoryCustom {
    public boolean existsByIdAtPlatform(String id, String platform);
}
