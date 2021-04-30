package com.assign.organization.domain.team.repository;

import com.assign.organization.domain.member.QMember;
import com.assign.organization.domain.team.QTeam;
import com.assign.organization.domain.team.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomTeamRepositoryImpl implements CustomTeamRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Team> findAllOrderByTeamNameAndMemberNameAndDuty() {
        return queryFactory
                .select(QTeam.team)
                .from(QTeam.team, QMember.member)
                .orderBy(QTeam.team.name.desc())
                .orderBy(QMember.member.name.desc())
                .orderBy(QMember.member.duty.desc())
                .fetch();
    }

    @Override
    public long countTeamNameDuplication(String teamName) {
        return queryFactory
                .select(QTeam.team.name)
                .from(QTeam.team)
                .where(QTeam.team.name.eq(teamName))
                .fetchCount();
    }

    @Override
    public Optional<Team> findByTeamName(String teamName) {
        Team team = queryFactory
                .selectFrom(QTeam.team)
                .where(QTeam.team.name.eq(teamName))
                .fetchOne();

        return Optional.ofNullable(team);
    }
}
