package com.footbolic.api.file.service;

import com.footbolic.api.file.dto.FileDto;
import com.footbolic.api.file.entity.FileEntity;
import com.footbolic.api.file.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    @Override
    public long count() {
        return fileRepository.count();
    }

    @Override
    public FileDto findById(String id) {
        return fileRepository.findById(id).map(FileEntity::toDto).orElse(null);
    }

    @Override
    public UrlResource getFile(FileDto file) {
        try {
            return new UrlResource("file:" + file.getPath() + file.getNewName() + "." + file.getExtension());
        } catch (MalformedURLException e) {
            log.error(e.toString());
            return null;
        }
    }

    @Override
    public FileDto save(MultipartFile file) {
        FileDto dto = toDto(file);
        transferFile(file, dto.getPath() + dto.getNewName() + "." + dto.getExtension());
        return fileRepository.save(dto.toEntity()).toDto();
    }

    @Override
    public void deleteFile(String id) {
        fileRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return fileRepository.existsById(id);
    }

    private FileDto toDto(MultipartFile file) {
        String extension = "";
        String originalName = "";
        if (file.getOriginalFilename() != null) {
            String[] slices = file.getOriginalFilename().split("\\.");
            originalName = slices[0];
            extension = slices[slices.length-1];
        }

        Date date = new Date();
        SimpleDateFormat newNameFormat = new SimpleDateFormat("yyyyMMddkkmmssSS");

        SimpleDateFormat directorySuffixFormat = new SimpleDateFormat("yyyy/MM/dd/");
        String directorySuffix = directorySuffixFormat.format(date);

        return FileDto.builder()
                .originalName(originalName)
                .newName(newNameFormat.format(date))
                .size(file.getSize())
                .type(file.getContentType())
                .extension(extension)
                .path(fileUploadDirectory + directorySuffix)
                .build();
    }

    private void transferFile(MultipartFile file, String pathname) {
        try {
            File saveDirectory = new File(pathname);
            if (!saveDirectory.getParentFile().exists()) saveDirectory.getParentFile().mkdirs();
            file.transferTo(saveDirectory);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

}
