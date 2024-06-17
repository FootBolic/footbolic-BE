package com.footbolic.api.file.repository;

import com.footbolic.api.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String>, FileRepositoryCustom {
}
