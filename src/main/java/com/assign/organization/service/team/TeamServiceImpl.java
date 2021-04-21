package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamDTO;
import com.assign.organization.domain.team.TeamRepository;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final MemberService memberService;

    @Override
    public TeamDTO findTeamById(Long id) {

        Optional<Team> findTeam = teamRepository.findById(id);

        if (findTeam.isEmpty()) {
            throw new NoResultException();
        }

        Team team = findTeam.get();

        return TeamDTO
                .builder()
                .name(team.getName())
                .teamLeader(team.getTeamLeaderDTO())
                .members(team.getTeamMembersDTO())
                .build();

    }

    @Override
    public TeamDTO createTeam(TeamVO newTeam) {

        Team team = Team
                .builder()
                .name(newTeam.getName())
                .build();

        Team savedTeam = teamRepository.save(team);

        return TeamDTO
                .builder()
                .name(savedTeam.getName())
                .build();
    }

    @Override
    public boolean deleteTeamByName(String name) {

        int deleteCount = teamRepository.deleteByName(name);

        return deleteCount > 0;
    }

    @Override
    public boolean updateTeamName(Long id, String name) {

        int exist = teamRepository.findNameExist(name);

        if (exist != 0) {
            return false;
        }

        Optional<Team> findTeam = teamRepository.findById(id);

        if (findTeam.isEmpty()) {
            throw new NoResultException();
        }

        Team team = findTeam.get();

        team.updateTeamName(name);

        return true;
    }

    @Override
    public void updateTeamLeader(Long teamId, Long teamLeaderId) {

        Optional<Member> findMember = memberService.findMemberByIdEntity(teamLeaderId);

        if(findMember.isEmpty()) {
            throw new NoResultException();
        }

        Member member = findMember.get();

        Optional<Team> findTeam = teamRepository.findById(teamId);

        if(findTeam.isEmpty()) {
            throw new NoResultException();
        }

        Team team = findTeam.get();

        team.changeTeamLeader(member);
    }
}
