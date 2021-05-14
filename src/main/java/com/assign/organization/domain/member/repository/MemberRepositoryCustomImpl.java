package com.assign.organization.domain.member.repository;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.QMember;
import com.assign.organization.domain.team.QTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public long countNameContains(String name) {

        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .where(member.lastName.concat(member.firstName).like(name + "%"))
                .fetchCount();
    }

    @Override
    public List<Member> findMembersContainsKeyword(String keyword) {
        QMember member = QMember.member;
        QTeam team = QTeam.team;

        return queryFactory
                .select(member)
                .from(member)
                .leftJoin(member.team, team)
                .fetchJoin()
                .where(
                        member.lastName.concat(member.firstName).contains(keyword)
                        .or(member.businessCall.contains(keyword)
                        .or(member.cellPhone.contains(keyword)))
                        .or(team.name.contains(keyword))
                )
                .orderBy(
                        team.name.asc(),
                        member.duty.desc(),
                        member.lastName.asc(),
                        member.firstName.asc()
                )
                .fetch();
    }
}
