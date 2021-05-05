package com.assign.organization.domain.member.repository;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.QMember;
import com.assign.organization.domain.team.QTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

    @Override
    public List<Member> findMembersContainsKeyword(String keyword) {
        QMember member = QMember.member;
        QTeam team = QTeam.team;

        return queryFactory
                .select(member)
                .from(member)
                .leftJoin(team)
                .on(member.team.eq(team))
                .where(
                        member.name.contains(keyword)
                        .or(member.contact.businessCall.contains(keyword)
                        .or(member.contact.cellPhone.contains(keyword)))
                        .or(team.name.contains(keyword))
                )
                .orderBy(
                        team.name.asc(),
                        member.duty.desc(),
                        member.name.asc()
                )
                .fetch();
    }
}
