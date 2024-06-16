package com.footbolic.api.image.service;

import com.footbolic.api.image.dto.ImageDto;
import com.footbolic.api.image.entity.ImageEntity;
import com.footbolic.api.image.repository.ImageRepository;
import com.footbolic.api.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final MemberService memberService;

    @Override
    public List<ImageDto> findAll(String commentId) {
        return imageRepository.findAll(commentId)
                .stream()
                .map(ImageEntity::toDto)
                .peek(e -> e.setCreatedBy(memberService.findById(e.getCreateMemberId()).toPublicDto()))
                .toList();
    }

    @Override
    public long count(String commentId) {
        return imageRepository.count(commentId);
    }

    @Override
    public ImageDto findById(String id) {
        return imageRepository.findById(id).map(ImageEntity::toDto).orElse(null);
    }

    @Override
    public ImageDto saveImage(ImageDto image) {
        return imageRepository.save(image.toEntity()).toDto();
    }

    @Override
    public void deleteImage(String id) {
        imageRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return imageRepository.existsById(id);
    }

}
