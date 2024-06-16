package com.footbolic.api.image.service;

import com.footbolic.api.image.dto.ImageDto;

import java.util.List;

public interface ImageService {

    List<ImageDto> findAll(String commentId);

    long count(String commentId);

    ImageDto findById(String id);

    ImageDto saveImage(ImageDto role);

    void deleteImage(String id);

    boolean existsById(String id);

}