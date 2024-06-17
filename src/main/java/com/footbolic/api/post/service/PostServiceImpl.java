package com.footbolic.api.post.service;

import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.comment.dto.CommentDto;
import com.footbolic.api.member.service.MemberService;
import com.footbolic.api.post.dto.PostDto;
import com.footbolic.api.post.entity.PostEntity;
import com.footbolic.api.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Optional<PostEntity> entity =  postRepository.findById(id);

        if (entity.isPresent()) {
            PostDto post = entity.get().toDto();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            post.setEditable(post.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG));
            post.setBoard(entity.get().getBoard().toDto());
            post.setComments(entity.get().getComments().stream().map(e -> {
                CommentDto comment = e.toDto();
                comment.setEditable(comment.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG));

                comment.getReplies().forEach(reply -> {
                    reply.setEditable(reply.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG));
                    reply.setCreatedBy(memberService.findById(reply.getCreateMemberId()).toPublicDto());
                });

                comment.setCreatedBy(memberService.findById(comment.getCreateMemberId()).toPublicDto());
                return comment;
            }).toList());
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
