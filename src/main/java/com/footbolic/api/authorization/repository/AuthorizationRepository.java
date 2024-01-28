package com.footbolic.api.authorization.repository;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRepository extends JpaRepository<AuthorizationEntity, String>, AuthorizationRepositoryCustom {
}
