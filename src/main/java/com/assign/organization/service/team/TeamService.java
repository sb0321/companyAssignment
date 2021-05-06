package com.assign.organization.service.team;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public List<TeamVO> findAllTeamListOrderByTeamNameDesc() {
        List<Team> teamList = teamRepository.findAllTeamsOrderByTeamName();
        teamList.forEach(t -> sortTeamMembersDutyAndName(t.getMembers()));
        return convertTeamListToTeamVOList(teamList);
    }

    private void sortTeamMembersDutyAndName(List<Member> memberList) {
        memberList.sort((m1, m2) -> {
            if (m1.getDuty().equals(m2.getDuty())) {
                return m1.getName().compareTo(m2.getName());
            }
            return m2.getDuty().compareTo(m1.getDuty());
        });
    }

    public List<TeamVO> convertTeamListToTeamVOList(List<Team> teamList) {
        List<TeamVO> teamVOList = new ArrayList<>();
        for (Team team : teamList) {

            List<MemberVO> memberVOList = convertMemberListToMemberVOList(team.getMembers());

            TeamVO vo = TeamVO
                    .builder()
                    .name(team.getName())
                    .id(team.getId())
                    .members(memberVOList)
                    .build();

            teamVOList.add(vo);
        }

        return teamVOList;
    }

    private List<MemberVO> convertMemberListToMemberVOList(Collection<Member> memberList) {
        return memberList
                .stream()
                .map(this::convertMemberToMemberVO)
                .collect(Collectors.toList());
    }

    private MemberVO convertMemberToMemberVO(Member member) {
        return MemberVO
                .builder()
                .id(member.getId())
                .name(member.getName())
                .teamName(member.getTeam().getName())
                .cellPhone(member.getContact().getCellPhone())
                .businessCall(member.getContact().getBusinessCall())
                .position(member.getPosition())
                .duty(member.getDuty())
                .build();
    }


    @Transactional
    public void changeMemberTeam(Team team, Member member) {
        member.changeTeam(team);
    }

    @Transactional
    public void insertTeamsFromTeamVOList(List<TeamVO> teamVOList) {
        for (TeamVO teamVO : teamVOList) {
            if (checkExistWithTeamName(teamVO.getName())) {
                continue;
            }

            Team team = convertTeamVOToEntity(teamVO);
            teamRepository.save(team);
        }
    }

    public Optional<Team> findTeamByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName);
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
}
