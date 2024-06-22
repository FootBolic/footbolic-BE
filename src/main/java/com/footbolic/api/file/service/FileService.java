package com.footbolic.api.file.service;

import com.footbolic.api.file.dto.FileDto;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    long count();

    FileDto findById(String id);

    UrlResource getFile(FileDto file);

    FileDto save(MultipartFile file);

    boolean delete(FileDto file);

    boolean existsById(String id);
}