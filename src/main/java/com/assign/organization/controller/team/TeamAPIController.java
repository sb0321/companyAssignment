package com.assign.organization.controller.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamAPIController {

    private final TeamService teamService;

    @GetMapping("")
    public List<TeamVO> getTeamsWithMembers() {
        List<Team> teamList = teamService.findAllTeamListOrderByTeamNameDesc();
        return convertTeamListToTeamVOList(teamList);
    }

    private List<TeamVO> convertTeamListToTeamVOList(List<Team> teamList) {
        return teamList
                .stream()
                .map(this::convertTeamToTeamVO)
                .collect(Collectors.toList());
    }

    private TeamVO convertTeamToTeamVO(Team team) {
        List<MemberVO> memberVOList = convertMemberListToMemberVOList(team.getMembers());
        memberVOList.sort(Comparator.comparing(MemberVO::getName));

        return TeamVO
                .builder()
                .id(team.getId())
                .name(team.getName())
                .members(memberVOList)
                .build();
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

}
