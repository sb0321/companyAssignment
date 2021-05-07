package com.assign.organization.domain.team.repository;

import com.assign.organization.domain.team.QTeam;
import com.assign.organization.domain.team.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Team> findAllTeamsOrderByTeamName() {

        QTeam team = QTeam.team;

        return queryFactory.selectFrom(team)
                .groupBy(team.id)
                .orderBy(team.name.asc())
                .fetch();
    }

    @Override
    public long countTeamNameDuplication(String teamName) {

        QTeam team = QTeam.team;

        return queryFactory
                .select(team.name)
                .from(team)
                .where(team.name.eq(teamName))
                .fetchCount();
    }

    @Override
    public Optional<Team> findByTeamName(String teamName) {

        QTeam team = QTeam.team;

        Team findTeam = queryFactory
                .selectFrom(team)
                .where(team.name.eq(teamName))
                .fetchOne();

        return Optional.ofNullable(findTeam);
    }
}