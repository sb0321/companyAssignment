package com.assign.organization.service.team;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public void insertTeams(Collection<Team> teams) {
        teamRepository.saveAll(teams);
    }

    public List<Team> findAllTeamListOrderByTeamNameDesc() {
        return teamRepository.findAllTeams();
    }

    @Transactional
    public void addMemberToTeam(Team team, Member member) {
        team.addTeamMember(member);
    }

    public List<TeamVO> extractTeamVOListFromCSVMemberVOList(List<CSVMemberVO> csvMemberVOList) {
        List<TeamVO> teamVOList = new ArrayList<>();

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {
            TeamVO teamVO = extractTeamVOFromCSVMemberVO(csvMemberVO);
            teamVOList.add(teamVO);
        }

        return teamVOList;
    }

    @Transactional
    public void insertTeamsFromTeamVOList(List<TeamVO> teamVOList) {
        for (TeamVO teamVO : teamVOList) {
            if (checkExistWithTeamName(teamVO.getName())) {
                continue;
            }
            createTeamWhenTeamNameNotDuplicated(teamVO);
        }
    }

    public Team findTeamByTeamName(String teamName) {
        Optional<Team> findTeam = teamRepository.findByTeamName(teamName);

        if (!findTeam.isPresent()) {
            throw new NoResultException();
        }

        return findTeam.get();
    }

    private Team convertTeamVOToEntity(TeamVO teamVO) {
        return Team
                .builder()
                .name(teamVO.getName())
                .build();
    }

    private boolean checkExistWithTeamName(String teamName) {
        long duplication = teamRepository.countTeamNameDuplication(teamName);
        return duplication != 0;
    }

    private void createTeamWhenTeamNameNotDuplicated(TeamVO teamVO) {
        Team newTeam = convertTeamVOToEntity(teamVO);

        try {
            teamRepository.save(newTeam);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private TeamVO extractTeamVOFromCSVMemberVO(CSVMemberVO csvMemberVO) {
        return TeamVO
                .builder()
                .name(csvMemberVO.getTeamName())
                .build();
    }
}
