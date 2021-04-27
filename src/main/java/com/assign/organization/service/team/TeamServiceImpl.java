package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamRepository;
import com.assign.organization.domain.team.TeamVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.*;

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
