package com.footbolic.api.icon.repository;

import com.footbolic.api.icon.entity.IconEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconRepository extends JpaRepository<IconEntity, String>, IconRepositoryCustom {
}
