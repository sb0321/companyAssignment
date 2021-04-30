package com.assign.organization.domain.member.repository;

import com.assign.organization.domain.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public long countNameContains(String name) {
        return queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.name.like(name + "%"))
                .fetchCount();
    }
}
