package com.assign.organization.controller.main;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.service.team.TeamService;
import com.assign.organization.utils.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainAPIController {

    @Value(value = "${csv.data}")
    private String csvFilePath;

    private final MemberService memberService;
    private final TeamService teamService;

    @GetMapping("/init")
    public void init() throws IOException {

        List<CSVMemberVO> csvMemberVOList = CSVReader.readCSVFile(csvFilePath);

        memberService.insertMembersFromCSVMemberVOList(csvMemberVOList);

        List<TeamVO> teamVOList = teamService.extractTeamVOListFromCSVMemberVOList(csvMemberVOList);
        teamService.insertTeamsFromTeamVOList(teamVOList);

        makeConnectionBetweenMemberAndTeamWithList(csvMemberVOList);
    }


    private void makeConnectionBetweenMemberAndTeamWithList(List<CSVMemberVO> csvMemberVOList) {
        for (CSVMemberVO csvMemberVO : csvMemberVOList) {
            Member findMember = memberService.findMemberById(csvMemberVO.getId());
            Team findTeam = teamService.findTeamByTeamName(csvMemberVO.getTeamName());

            teamService.addMemberToTeam(findTeam, findMember);
        }
    }

}
