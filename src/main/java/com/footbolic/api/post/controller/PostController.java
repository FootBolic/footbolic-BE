package com.footbolic.api.post.controller;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.post.dto.PostDto;
import com.footbolic.api.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "게시글 API")
@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
@Slf4j
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping
    public SuccessResponse getPostList(
            Pageable pageable,
            @RequestParam(name = "boardId", required = false) String boardId,
            @RequestParam(name = "searchTitle", required = false) String searchTitle,
            @RequestParam(name = "searchCreatedBy", required = false) String searchCreatedBy,
            @RequestParam(name = "searchCreatedAt", required = false) LocalDateTime searchCreatedAt
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("posts", postService.findAll(boardId, pageable, searchTitle, searchCreatedBy, searchCreatedAt));
        result.put("size", postService.count(boardId, searchTitle, searchCreatedBy, searchCreatedAt));

        return new SuccessResponse(result);
    }

    @Operation(summary = "게시글 생성", description = "파라미터로 전달 받은 게시글을 생성한다.")
    @Parameter(name = "post", description = "생성할 게시글 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PostMapping
    public ResponseEntity<BaseResponse> createPost(
            @RequestBody @Valid PostDto post
    ) {
        return ResponseEntity.ok(new SuccessResponse(Map.of("createdPost", postService.savePost(post))));
    }

    @Operation(summary = "게시글 단건 조회", description = "전달 받은 식별번호를 가진 게시글을 조회한다.")
    @Parameter(name = "id", description = "게시글 식별번호", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getPost(
            @PathVariable(name = "id") String id
    ) {
        PostDto post = postService.findById(id);

        if (post != null) return ResponseEntity.ok(new SuccessResponse(Map.of("post", post)));
        else return ResponseEntity.badRequest().body(new ErrorResponse("조회된 게시글이 없습니다."));
    }

    @Operation(summary = "게시글 수정", description = "파라미터로 전달 받은 게시글을 수정한다.")
    @Parameter(name = "post", description = "수정할 게시글 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PatchMapping
    public ResponseEntity<BaseResponse> updatePost(
            @RequestBody @Valid PostDto post,
            Authentication authentication
    ) {
        if (post.getId() == null || post.getId().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 게시글 정보입니다."));
        } else if (postService.existsById(post.getId())) {
            PostDto target = postService.findById(post.getId());

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {
                PostDto updatedPost = postService.savePost(post);

                return ResponseEntity.ok(new SuccessResponse(Map.of("updatedPost", updatedPost)));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("수정할 권한이 없는 게시글입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 게시글이 없습니다."));
        }
    }

    @Operation(summary = "게시글 삭제", description = "제공된 식별번호를 가진 게시글을 삭제한다.")
    @Parameter(name = "post", description = "수정할 게시글 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletePost(
            @PathVariable(name = "id") String id,
            Authentication authentication
    ) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 게시글 식별번호입니다."));
        } else if (postService.existsById(id)) {
            PostDto target = postService.findById(id);

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {
                postService.deletePost(id);

                return ResponseEntity.ok(new SuccessResponse(Map.of("id", id)));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("수정할 권한이 없는 게시글입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 게시글이 없습니다."));
        }
    }

    @Operation(summary = "전체 게시판 인기 게시글 목록 조회", description = "전체 게시판 인기 게시글 목록을 limit 만큼 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/public/hot")
    public SuccessResponse getHotPostList(
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
    ) {
        return new SuccessResponse(Map.of("posts", postService.findHotPosts(limit)));
    }

    @Operation(summary = "전체 게시판 최신 게시글 목록 조회", description = "전체 게시판 최신 게시글 목록을 limit 만큼 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/public/new")
    public SuccessResponse getNewPostList(
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
    ) {
        return new SuccessResponse(Map.of("posts", postService.findNewPosts(limit)));
    }

    @Operation(summary = "게시판 별 최신 게시글 목록 조회", description = "게시판 별 최신 게시글 목록을 limit 만큼 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/public/new/{boardId}")
    public SuccessResponse getNewPostListByBoard(
            @PathVariable(name = "boardId") String boardId,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
    ) {
        return new SuccessResponse(Map.of("posts", postService.findNewPostsByBoard(boardId, limit)));
    }
}
