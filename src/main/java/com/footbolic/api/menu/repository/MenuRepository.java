package com.footbolic.api.menu.repository;

import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.menu.entity.MenuEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, String>, MenuRepositoryCustom {

//    @Query("SELECT" +
//            " new com.footbolic.api.menu.dto.MenuDto(" +
//            "   e.id," +
//            "   e.parentId," +
//            "   e.title," +
//            "   e.path," +
//            "   e.iconCodeId," +
//            "   e.createdAt," +
//            "   e.createMemberId," +
//            "   e.updatedAt," +
//            "   e.updateMemberId" +
//            ") " +
//            "FROM MenuEntity e")
//    List<MenuDto> findAllMenus();

}
