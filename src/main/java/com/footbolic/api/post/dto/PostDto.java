package com.footbolic.api.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.footbolic.api.board.dto.BoardDto;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.post.entity.PostEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(name = "게시글 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private String id;

    private String boardId;

    private BoardDto board;

    private String title;

    private String content;

    @JsonProperty("isSecret")
    private boolean isSecret;

    @JsonProperty("isAnnouncement")
    private boolean isAnnouncement;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime announcementStartsAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime announcementEndsAt;

//    private List<CommentEntity> comments = new ArrayList<>();

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    private String createMemberId;

    private MemberDto createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    private String updateMemberId;

    private MemberDto updatedBy;

    public PostEntity toEntity() {
        return PostEntity.builder()
                .id(id)
                .title(title)
                .content(content)
                .boardId(boardId)
                .board(board == null ? null : board.toEntity())
                .isSecret(isSecret)
                .isAnnouncement(isAnnouncement)
                .announcementStartsAt(announcementStartsAt)
                .announcementEndsAt(announcementEndsAt)
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy == null ? null : createdBy.toEntity())
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy == null ? null : updatedBy.toEntity())
                .build();
    }
}
