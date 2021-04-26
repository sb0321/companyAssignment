package com.assign.organization.controller;

import com.assign.organization.domain.member.CSVMemberDTO;
import com.assign.organization.domain.member.Member;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InitController {

    private final static String DATA_PATH = "/Users/sbkim/Downloads/data.csv";

    private final static String TEAM_LEADER_DUTY = "팀장";

    private final MemberService memberService;
    private final TeamService teamService;

    @Transactional
    @GetMapping("/init")
    public void initTest() {

        List<CSVMemberDTO> csvData = CSVUtil.getCSVData(DATA_PATH);

        if (csvData == null) {
            return;
        }

        List<Member> memberList = memberService.initMembers(csvData);

        Map<String, Team> teams = new HashMap<>();

        for (int i = 0; i < csvData.size(); i++) {

            CSVMemberDTO now = csvData.get(i);

            Team nowTeam;

            if (teams.containsKey(now.getTeamName())) {
                nowTeam = teams.get(now.getTeamName());
            } else {
                nowTeam = teamService.createTeam(TeamVO
                        .builder()
                        .name(now.getTeamName())
                        .build());

                teams.put(now.getTeamName(), nowTeam);
            }

            Member member = memberList.get(i);

            member.changeTeam(nowTeam);

            if(now.getDuty().equals(TEAM_LEADER_DUTY)) {
                nowTeam.changeTeamLeader(member);
            }

        }

    }
}