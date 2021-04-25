package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamDTO;
import com.assign.organization.domain.team.TeamRepository;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.member.MemberService;
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

    private final MemberService memberService;

    @Override
    public TeamDTO findTeamById(Long id) {

        Optional<Team> findTeam = teamRepository.findById(id);

        if (!findTeam.isPresent()) {
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
    public Team createTeam(TeamVO newTeam) {

        Team team = Team
                .builder()
                .name(newTeam.getName())
                .build();

        return teamRepository.save(team);
    }

    @Override
    public List<TeamVO> getTeamList() {

        List<Team> allTeam = teamRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));

        List<TeamVO> teamVOList = new ArrayList<>();

        for (Team team : allTeam) {

            Member teamLeaderEntity = team.getTeamLeader();

            MemberVO leader;

            if(teamLeaderEntity == null) {
                leader = null;
            } else {
                leader = MemberVO
                        .builder()
                        .id(teamLeaderEntity.getId())
                        .businessCall(teamLeaderEntity.getAddress().getBusinessCall())
                        .cellPhone(teamLeaderEntity.getAddress().getCellPhone())
                        .name(teamLeaderEntity.getName())
                        .duty(teamLeaderEntity.getDuty())
                        .position(teamLeaderEntity.getPosition())
                        .build();
            }

            List<MemberVO> teamMembers = new ArrayList<>();

            for (Member member : team.getMembers()) {

                if(leader != null && member.getId().equals(leader.getId())) {
                    continue;
                }

                MemberVO vo = MemberVO
                        .builder()
                        .id(member.getId())
                        .businessCall(member.getAddress().getBusinessCall())
                        .cellPhone(member.getAddress().getCellPhone())
                        .name(member.getName())
                        .duty(member.getDuty())
                        .position(member.getPosition())
                        .build();

                teamMembers.add(vo);
            }

            TeamVO vo = TeamVO
                    .builder()
                    .id(team.getId())
                    .teamLeader(leader)
                    .members(teamMembers)
                    .name(team.getName())
                    .build();

            teamVOList.add(vo);
        }

        return teamVOList;
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

        if (!findTeam.isPresent()) {
            throw new NoResultException();
        }

        Team team = findTeam.get();

        team.updateTeamName(name);

        return true;
    }

    @Override
    public void updateTeamLeader(Long teamId, Long teamLeaderId) {

        Optional<Member> findMember = memberService.findMemberByIdEntity(teamLeaderId);

        if(!findMember.isPresent()) {
            throw new NoResultException();
        }

        Member member = findMember.get();

        Optional<Team> findTeam = teamRepository.findById(teamId);

        if(!findTeam.isPresent()) {
            throw new NoResultException();
        }

        Team team = findTeam.get();

        team.changeTeamLeader(member);
    }
}
