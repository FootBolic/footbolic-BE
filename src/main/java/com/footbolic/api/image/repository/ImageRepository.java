package com.footbolic.api.image.repository;

import com.footbolic.api.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, String>, ImageRepositoryCustom {
}
