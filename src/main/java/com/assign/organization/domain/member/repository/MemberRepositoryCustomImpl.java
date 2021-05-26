package com.assign.organization.domain.member.repository;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.QMember;
import com.assign.organization.domain.team.QTeam;
import com.assign.organization.exception.NullBusinessCallException;
import com.assign.organization.exception.NullMemberIdException;
import com.assign.organization.exception.NullMemberNameException;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countFirstNameContains(String firstName) {
        if (firstName == null) {
            throw new NullMemberNameException();
        }

        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .where(member.firstName.like(firstName + "%"))
                .fetchCount();
    }

    @Override
    public List<Member> findMembersContainsKeyword(String keyword) {

        QMember member = QMember.member;
        QTeam team = QTeam.team;

        JPAQuery<Member> query = queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .fetchJoin()
                .orderBy(
                        team.name.asc(),
                        member.duty.desc(),
                        member.lastName.asc(),
                        member.firstName.asc()
                );

        if (keyword == null || keyword.isEmpty()) {
            return query.fetch();
        }

        return query.where(
                member.lastName.concat(member.firstName).contains(keyword)
                        .or(member.businessCall.contains(keyword)
                                .or(member.cellPhone.contains(keyword)))
                        .or(team.name.contains(keyword))
        )
                .fetch();
    }

    @Override
    public boolean checkMemberIdDuplication(Long memberId) {

        if (memberId == null) {
            throw new NullMemberIdException();
        }

        QMember member = QMember.member;

        long count = queryFactory.selectFrom(member)
                .where(member.id.eq(memberId))
                .fetchCount();

        return count != 0;
    }

    @Override
    public boolean checkBusinessCallDuplication(String businessCall) {

        if (businessCall == null) {
            throw new NullBusinessCallException();
        }

        QMember member = QMember.member;

        long count = queryFactory.selectFrom(member)
                .where(member.businessCall.eq(businessCall))
                .fetchCount();

        return count != 0;
    }
}
