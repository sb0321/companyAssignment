package com.assign.organization.service.team;

import com.assign.organization.domain.team.TeamDTO;

public interface TeamService {

    TeamDTO findTeamById(Long id);

    boolean deleteTeamByName(String name);

    boolean updateTeamName(Long id, String name);

    void updateTeamLeader(Long teamId, Long teamLeaderId);
}
