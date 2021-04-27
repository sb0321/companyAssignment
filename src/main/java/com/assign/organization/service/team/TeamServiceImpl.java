package com.assign.organization.service.team;

import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamRepository;
import com.assign.organization.domain.team.TeamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public Team createTeam(TeamVO newTeam) {

        Team team = Team
                .builder()
                .name(newTeam.getName())
                .build();

        return teamRepository.save(team);
    }
}
