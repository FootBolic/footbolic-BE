package com.footbolic.api.recommendation.controller;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.recommendation.dto.CommentRecommendationDto;
import com.footbolic.api.recommendation.dto.PostRecommendationDto;
import com.footbolic.api.recommendation.dto.ReplyRecommendationDto;
import com.footbolic.api.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "추천 API")
@RequestMapping("/recommendations")
@RequiredArgsConstructor
@RestController
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "추천 목록 조회", description = "추천 목록을 page 단위로 조회")
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping("/{type}/{objectId}")
    public ResponseEntity<BaseResponse> getRecommendationList(
            @PathVariable(name = "type") String type,
            @PathVariable(name = "objectId") String objectId,
            Authentication authentication
    ) {
        Map<String, Object> result = new HashMap<>();
        boolean isRecommended = false;

        switch (type) {
            case "comment":
                List<CommentRecommendationDto> commentRecommendations = recommendationService.findAllCommentRecommendations(objectId);
                result.put("recommendations", commentRecommendations);

                if (authentication != null && commentRecommendations != null) {
                    for (CommentRecommendationDto recommendation : commentRecommendations) {
                        if (recommendation.getMemberId().equals(authentication.getCredentials().toString())) {
                            isRecommended = true;
                            break;
                        }
                    }
                }

                result.put("isRecommended", isRecommended);
                result.put("size", recommendationService.countCommentRecommendations(objectId));
                break;
            case "post":
                List<PostRecommendationDto> postRecommendations =  recommendationService.findAllPostRecommendations(objectId);
                result.put("recommendations", postRecommendations);

                if (authentication != null && postRecommendations != null) {
                    for (PostRecommendationDto recommendation : postRecommendations) {
                        if (recommendation.getMemberId().equals(authentication.getCredentials().toString())) {
                            isRecommended = true;
                            break;
                        }
                    }
                }

                result.put("isRecommended", isRecommended);
                result.put("size", recommendationService.countPostRecommendations(objectId));
                break;
            case "reply":
                List<ReplyRecommendationDto> replyRecommendations = recommendationService.findAllReplyRecommendations(objectId);
                result.put("recommendations", replyRecommendations);

                if (authentication != null && replyRecommendations != null) {
                    for (ReplyRecommendationDto recommendation : replyRecommendations) {
                        if (recommendation.getMemberId().equals(authentication.getCredentials().toString())) {
                            isRecommended = true;
                            break;
                        }
                    }
                }

                result.put("isRecommended", isRecommended);
                result.put("size", recommendationService.countReplyRecommendations(objectId));
                break;
            default:
                return ResponseEntity.badRequest().body(new ErrorResponse("잘못된 요청입니다."));
        }

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "추천 생성", description = "파라미터로 전달 받은 추천을 생성한다.")
    @Parameter(name = "recommendation", description = "생성할 추천 객체", required = true)
    @Parameter
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PostMapping("/{type}/{objectId}")
    public ResponseEntity<BaseResponse> createRecommendation(
            @PathVariable(name = "type") String type,
            @PathVariable(name = "objectId") String objectId,
            Authentication authentication
    ) {
        String memberId = authentication.getCredentials().toString();
        Map<String, Object> result = new HashMap<>();

        switch (type) {
            case "comment":
                if (!recommendationService.commentRecommendationExists(memberId, objectId)) {
                    result.put("createdRecommendation", recommendationService.saveCommentRecommendation(
                            CommentRecommendationDto.builder()
                                    .memberId(memberId)
                                    .commentId(objectId)
                                    .build()
                    ));
                } else {
                    return ResponseEntity.badRequest().body(new ErrorResponse("이미 추천된 댓글입니다."));
                }
                break;
            case "post":
                if (!recommendationService.postRecommendationExists(memberId, objectId)) {
                    result.put("createdRecommendation", recommendationService.savePostRecommendation(
                            PostRecommendationDto.builder()
                                    .memberId(memberId)
                                    .postId(objectId)
                                    .build()
                    ));
                } else {
                    return ResponseEntity.badRequest().body(new ErrorResponse("이미 추천된 게시글입니다."));
                }
                break;
            case "reply":
                if (!recommendationService.replyRecommendationExists(memberId, objectId)) {
                    result.put("createdRecommendation", recommendationService.saveReplyRecommendation(
                            ReplyRecommendationDto.builder()
                                    .memberId(memberId)
                                    .replyId(objectId)
                                    .build()
                    ));
                } else {
                    return ResponseEntity.badRequest().body(new ErrorResponse("이미 추천된 답글입니다."));
                }
                break;
            default:
                return ResponseEntity.badRequest().body(new ErrorResponse("잘못된 요청입니다."));
        }

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "추천 삭제", description = "제공된 식별번호를 가진 추천을 삭제한다.")
    @Parameter(name = "recommendation", description = "수정할 추천 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @DeleteMapping("/{type}/{objectId}")
    public ResponseEntity<BaseResponse> deleteRecommendation(
            @PathVariable(name = "type") String type,
            @PathVariable(name = "objectId") String objectId,
            Authentication authentication
    ) {
        String memberId = authentication.getCredentials().toString();
        Map<String, Object> result = new HashMap<>();

        switch (type) {
            case "comment":
                if (recommendationService.commentRecommendationExists(memberId, objectId)) {
                    recommendationService.deleteCommentRecommendation(memberId, objectId);
                    result.put("id", objectId);
                } else {
                    return ResponseEntity.badRequest().body(new ErrorResponse("이미 추천된 댓글입니다."));
                }
                break;
            case "post":
                if (recommendationService.postRecommendationExists(memberId, objectId)) {
                    recommendationService.deletePostRecommendation(memberId, objectId);
                    result.put("id", objectId);
                } else {
                    return ResponseEntity.badRequest().body(new ErrorResponse("이미 추천된 게시글입니다."));
                }
                break;
            case "reply":
                if (recommendationService.replyRecommendationExists(memberId, objectId)) {
                    recommendationService.deleteReplyRecommendation(memberId, objectId);
                    result.put("id", objectId);
                } else {
                    return ResponseEntity.badRequest().body(new ErrorResponse("이미 추천된 답글입니다."));
                }
                break;
            default:
                return ResponseEntity.badRequest().body(new ErrorResponse("잘못된 요청입니다."));
        }

        return ResponseEntity.ok(new SuccessResponse(result));
    }
}
