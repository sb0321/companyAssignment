package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public List<Team> findAllTeamListOrderByTeamNameDesc() {
        return teamRepository.findAllOrderByTeamNameAndMemberNameAndDuty();
    }

    @Transactional
    public void addMemberToTeam(Team team, Member member) {
        team.addTeamMember(member);
    }

    public boolean checkExistWithTeamName(String teamName) {
        long duplication = teamRepository.countTeamNameDuplication(teamName);
        return duplication != 0;
    }

    public Team findTeamByTeamName(String teamName) {
        Optional<Team> findTeam = teamRepository.findByTeamName(teamName);

        if (!findTeam.isPresent()) {
            throw new NoResultException();
        }

        return findTeam.get();
    }

    @Transactional
    public void createTeamWhenTeamNameNotDuplicated(TeamVO teamVO) {
        Team newTeam = convertTeamVOToEntity(teamVO);

        try {
            teamRepository.save(newTeam);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private Team convertTeamVOToEntity(TeamVO teamVO) {
        return Team
                .builder()
                .name(teamVO.getName())
                .build();
    }

}
