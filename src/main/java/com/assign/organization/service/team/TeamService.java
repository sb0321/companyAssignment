package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamDTO;
import com.assign.organization.domain.team.TeamVO;

import java.util.List;
import java.util.Set;

public interface TeamService {

    TeamDTO findTeamById(Long id);

    Team createTeam(TeamVO newTeam);

    List<Team> initTeams(Set<String> teamNames);

    List<TeamVO> getTeamList();

    boolean deleteTeamByName(String name);

}
