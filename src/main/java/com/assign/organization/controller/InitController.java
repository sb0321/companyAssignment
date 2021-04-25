package com.assign.organization.controller;

import com.assign.organization.domain.member.CSVMemberDTO;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.service.team.TeamService;
import com.assign.organization.utils.CSVUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InitController {

    private final static String DATA_PATH = "D:\\download\\data.csv";

    private final static String TEAM_LEADER_DUTY = "팀장";

    private final MemberService memberService;
    private final TeamService teamService;

    @Transactional
    @GetMapping("/init")
    public String init() {
        List<CSVMemberDTO> csvData = CSVUtil.getCSVData(DATA_PATH);

        log.info(csvData.toString());

        Set<String> teamNames = new HashSet<>();
        Map<String, Long> teamLeader = new HashMap<>();
        Map<String, Set<Long>> members = new HashMap<>();

        for (CSVMemberDTO csvMember : csvData) {

            teamNames.add(csvMember.getTeamName());

            if(csvMember.getDuty().equals(TEAM_LEADER_DUTY)) {
                teamLeader.putIfAbsent(csvMember.getTeamName(), csvMember.getId());
            }

            if(members.containsKey(csvMember.getTeamName())) {
                members.get(csvMember.getTeamName()).add(csvMember.getId());
            } else {
                Set<Long> temp = new HashSet<>();
                temp.add(csvMember.getId());
                members.put(csvMember.getTeamName(), temp);
            }

            MemberVO newMember = MemberVO
                    .builder()
                    .id(csvMember.getId())
                    .name(csvMember.getName())
                    .duty(csvMember.getDuty())
                    .position(csvMember.getPosition())
                    .businessCall(csvMember.getBusinessCall())
                    .cellPhone(csvMember.getCellPhone())
                    .build();

            memberService.createMember(newMember);
        }

        for (String teamName : teamNames) {

            TeamVO newTeam = TeamVO
                    .builder()
                    .name(teamName)
                    .build();

            Team team = teamService.createTeam(newTeam);

            Set<Long> teamMembers = members.get(teamName);

            for (Long memberId : teamMembers) {
                log.info(memberId + " " + teamName);
                Member member = memberService.findMemberByIdEntity(memberId).get();
                team.addMember(member);
            }

            if(teamLeader.getOrDefault(teamName, null) != null) {
                team.changeTeamLeader(memberService.findMemberByIdEntity(teamLeader.get(teamName)).get());
            }
        }
        log.info(members.toString());
        log.info(teamNames.toString());
        log.info(teamLeader.toString());

        return "hello";
    }

}
