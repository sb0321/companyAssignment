package com.assign.organization.domain.team.repository;

import com.assign.organization.domain.team.Team;

import java.util.List;
import java.util.Optional;

public interface CustomTeamRepository {

    List<Team> findAllOrderByTeamNameAndMemberNameAndDuty();

    long countTeamNameDuplication(String teamName);

    Optional<Team> findByTeamName(String teamName);
}
