package com.footbolic.api.reply.repository;

import com.footbolic.api.reply.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, String>, ReplyRepositoryCustom {
}
