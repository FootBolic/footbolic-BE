package com.footbolic.api.recommendation.entity;

import com.footbolic.api.common.entity.BaseEntity;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.recommendation.dto.RecommendationDto;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class RecommendationEntity extends BaseEntity {

    @Column(name = "member_id", nullable = false, updatable = false, length = 30)
    private String memberId;

    @Transient
    private MemberEntity member;

    public abstract RecommendationDto toDto();

}
