package com.footbolic.api.common.entity;

import com.footbolic.api.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class ExtendedBaseEntity extends BaseEntity {

    @Column(name = "create_member_id", nullable = false, updatable = false, length = 30)
    private String createMemberId;

    @Transient
    private MemberEntity createdBy;

    @Builder.Default
    @Column(name = "update_member_id", length = 30)
    private String updateMemberId = null;

    @Transient
    private MemberEntity updatedBy;

//    @PreUpdate
//    public void preUpdate() {
//        this.update_member_id;
//    }

}
