package com.footbolic.api.common.entity;

import com.footbolic.api.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor
@NoArgsConstructor
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

    @PrePersist
    public void PrePersist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            this.createMemberId = authentication.getCredentials().toString();
        }
    }

    @PreUpdate
    public void preUpdate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            this.updateMemberId = authentication.getCredentials().toString();
        }
    }

}
