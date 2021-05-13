package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        return convertTeamListToTeamVOList(teamList);
    }

    private List<TeamVO> convertTeamListToTeamVOList(List<Team> teamList) {
        List<TeamVO> teamVOList = new ArrayList<>();
        for (Team team : teamList) {

            List<MemberVO> memberVOList = convertMemberListToMemberVOList(team.getMembers());
            TeamVO vo = new TeamVO(team.getId(), team.getName(), memberVOList);
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
                .cellPhone(member.getCellPhone())
                .businessCall(member.getBusinessCall())
                .position(member.getPosition())
                .duty(member.getDuty())
                .build();
    }

    public Optional<Team> findTeamByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName);
    }
}
