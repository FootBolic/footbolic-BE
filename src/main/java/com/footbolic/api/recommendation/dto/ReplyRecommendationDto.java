package com.footbolic.api.recommendation.dto;

import com.footbolic.api.recommendation.entity.ReplyRecommendationEntity;
import com.footbolic.api.reply.dto.ReplyDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(name = "답글 추천 DTO")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRecommendationDto extends RecommendationDto {

    private String replyId;

    private ReplyDto reply;

    @Override
    public ReplyRecommendationEntity toEntity() {
        return ReplyRecommendationEntity.builder()
                .id(getId())
                .memberId(getMemberId())
                .replyId(replyId)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
