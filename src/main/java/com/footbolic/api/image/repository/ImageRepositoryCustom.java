package com.footbolic.api.image.repository;

import com.footbolic.api.image.entity.ImageEntity;

import java.util.List;

public interface ImageRepositoryCustom {

    List<ImageEntity> findAll(String commentId);

    long count(String commentId);

}
