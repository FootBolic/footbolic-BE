package com.footbolic.api.program.repository;

import com.footbolic.api.program.entity.ProgramEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.program.entity.QProgramEntity.programEntity;

@Repository
@RequiredArgsConstructor
public class ProgramRepositoryCustomImpl implements ProgramRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProgramEntity> findAll(Pageable pageable, String searchTitle, String searchCode) {

        JPAQuery<ProgramEntity> query = queryFactory.selectFrom(programEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(programEntity.title.contains(searchTitle));
        }

        if (searchCode != null && !searchCode.isBlank()) {
            query.where(programEntity.code.eq(searchCode));
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(programEntity.id.desc())
                .fetch();
    }

    @Override
    public long count(String searchTitle, String searchCode) {
        JPAQuery<ProgramEntity> query = queryFactory.selectFrom(programEntity);

        if (searchTitle != null && !searchTitle.isBlank()) {
            query.where(programEntity.title.contains(searchTitle));
        }

        if (searchCode != null && !searchCode.isBlank()) {
            query.where(programEntity.code.eq(searchCode));
        }

        return query.fetch().size();
    }
}
