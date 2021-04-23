package com.assign.organization.service.team;

import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamDTO;
import com.assign.organization.domain.team.TeamVO;

import java.util.List;

public interface TeamService {

    TeamDTO findTeamById(Long id);

    Team createTeam(TeamVO newTeam);

    List<TeamVO> getTeamList();

    boolean deleteTeamByName(String name);

    boolean updateTeamName(Long id, String name);

    void updateTeamLeader(Long teamId, Long teamLeaderId);
}
