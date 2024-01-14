package com.footbolic.api.menu.repository;

import com.footbolic.api.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, String>, MenuRepositoryCustom {
}
