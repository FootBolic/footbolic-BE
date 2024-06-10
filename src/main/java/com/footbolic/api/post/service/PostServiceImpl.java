package com.footbolic.api.post.service;

import com.footbolic.api.member.service.MemberService;
import com.footbolic.api.post.dto.PostDto;
import com.footbolic.api.post.entity.PostEntity;
import com.footbolic.api.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final MemberService memberService;

    @Override
    public List<PostDto> findAll(String boardId, Pageable pageable, String searchTitle , String searchCreatedBy, LocalDateTime searchCreatedAt) {
        return postRepository.findAll(boardId, pageable, searchTitle, searchCreatedBy, searchCreatedAt)
                .stream()
                .map(PostEntity::toDto)
                .peek(e -> e.setCreatedBy(memberService.findById(e.getCreateMemberId()).toPublicDto()))
                .toList();
    }

    @Override
    public long count(String boardId, String searchTitle, String searchCreatedBy, LocalDateTime searchCreatedAt) {
        return postRepository.count(boardId, searchTitle, searchCreatedBy, searchCreatedAt);
    }

    @Override
    public PostDto findById(String id) {
        Optional<PostEntity> opt =  postRepository.findById(id);

        if (opt.isPresent()) {
            PostDto post = opt.get().toDto();
            post.setBoard(opt.get().getBoard().toDto());
            post.setCreatedBy(memberService.findById(post.getCreateMemberId()).toPublicDto());

            return post;
        } else {
            return null;
        }
    }

    @Override
    public PostDto savePost(PostDto post) {
        return postRepository.save(post.toEntity()).toDto();
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
