package com.footbolic.api.banner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.footbolic.api.file.dto.FileDto;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.banner.entity.BannerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(name = "배너 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDto {

    private String id;

    private String title;

    private String fileId;

    private FileDto file;

    private String link;

    @JsonProperty("isMobile")
    private boolean isMobile;

    @JsonProperty("isTimeLimited")
    private boolean isTimeLimited;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startsAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endsAt;

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

    public BannerEntity toEntity() {
        return BannerEntity.builder()
                .id(id)
                .title(title)
                .fileId(fileId)
                .link(link)
                .isMobile(isMobile)
                .isTimeLimited(isTimeLimited)
                .startsAt(startsAt)
                .endsAt(endsAt)
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy == null ? null : createdBy.toEntity())
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy == null ? null : updatedBy.toEntity())
                .build();
    }

}
