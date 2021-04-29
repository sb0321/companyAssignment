package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.QTeam;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamRepository;
import com.assign.organization.domain.team.TeamVO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public List<Team> findAllTeamListOrderByTeamNameDesc() {
        OrderSpecifier<String> orderSpecifier = getTeamNameDescOrderSpecifier();
        Iterable<Team> teams = teamRepository.findAll(orderSpecifier);
        return convertIterableTeamToList(teams);
    }

    private List<Team> convertIterableTeamToList(Iterable<Team> teams) {
        List<Team> convertedResult = new ArrayList<>();
        for (Team team : teams) {
            convertedResult.add(team);
        }
        return convertedResult;
    }

    private OrderSpecifier<String> getTeamNameDescOrderSpecifier() {
        return QTeam.team.name.desc();
    }

    @Transactional
    public void addMemberToTeam(Team team, Member member) {
        team.addTeamMember(member);
    }

    @Transactional
    public void changeTeamLeader(Team team, Member member) {
        team.changeTeamLeader(member);
    }

    public boolean checkExistWithTeamName(String teamName) {
        Predicate predicate = makeTeamNameDuplicationCheckPredicate(teamName);
        return teamRepository.exists(predicate);
    }

    private Predicate makeTeamNameDuplicationCheckPredicate(String teamName) {
        return QTeam.team.name.eq(teamName);
    }

    public Team findTeamByTeamName(String teamName) {
        Predicate predicate = makeTeamNameSearchPredicate(teamName);
        Optional<Team> findTeam = teamRepository.findOne(predicate);

        if (!findTeam.isPresent()) {
            throw new NoResultException();
        }

        return findTeam.get();
    }

    private Predicate makeTeamNameSearchPredicate(String teamName) {
        return QTeam.team.name.like(teamName);
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
