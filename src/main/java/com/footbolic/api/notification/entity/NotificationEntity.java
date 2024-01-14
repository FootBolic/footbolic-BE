package com.footbolic.api.notification.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity(name = "NotificationEntity")
@Table(name = "Notification")
public class NotificationEntity extends ExtendedBaseEntity {

    @Column(name = "member_id", nullable = false, length = 30)
    private String memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private MemberEntity member;

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "path", length = 100)
    private String path;

}
