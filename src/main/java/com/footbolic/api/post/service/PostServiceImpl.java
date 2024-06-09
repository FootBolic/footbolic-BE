package com.footbolic.api.post.service;

import com.footbolic.api.post.dto.PostDto;
import com.footbolic.api.post.entity.PostEntity;
import com.footbolic.api.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<PostDto> findAll(String boardId, Pageable pageable, String searchTitle) {
        return postRepository.findAll(boardId, pageable, searchTitle)
                .stream()
                .map(PostEntity::toDto)
                .toList();
    }

    @Override
    public long count(String boardId, String searchTitle) {
        return postRepository.count(boardId, searchTitle);
    }

    @Override
    public PostDto findById(String id) {
        return postRepository.findById(id).map(PostEntity::toDto).orElse(null);
    }

    @Override
    public PostDto savePost(PostDto role) {
        PostEntity createdPost = postRepository.save(role.toEntity());
        return createdPost.toDto();
    }

    @Override
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return postRepository.existsById(id);
    }

}
