package com.assign.organization.controller.main;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.service.team.TeamService;
import com.assign.organization.utils.CSVReader;
import com.assign.organization.utils.NameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
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

        createMembersFromCSVMemberVOList(csvMemberVOList);

        List<TeamVO> teamVOList = extractTeamVOListFromCSVMemberVOList(csvMemberVOList);
        createTeamsFromTeamVOList(teamVOList);

        makeConnectionBetweenMemberAndTeamWithList(csvMemberVOList);
    }


    private void makeConnectionBetweenMemberAndTeamWithList(List<CSVMemberVO> csvMemberVOList) {
        csvMemberVOList.forEach(this::makeConnectionBetweenMemberAndTeam);
    }

    private void makeConnectionBetweenMemberAndTeam(CSVMemberVO csvMemberVO) {
        Member findMember = memberService.findMemberById(csvMemberVO.getId());
        Team findTeam = teamService.findTeamByTeamName(csvMemberVO.getTeamName());

        teamService.addMemberToTeam(findTeam, findMember);
    }

    private void createMembersFromCSVMemberVOList(List<CSVMemberVO> csvMemberVOList) {
        List<MemberVO> memberVOList = convertCSVMemberVOListToMemberVOList(csvMemberVOList);
        createMembersFromMemberVOList(memberVOList);
    }

    private TeamVO extractTeamVOFromCSVMemberVO(CSVMemberVO csvMemberVO) {
        return TeamVO
                .builder()
                .name(csvMemberVO.getTeamName())
                .build();
    }

    private void createMembersFromMemberVOList(List<MemberVO> memberVOList) {
        for (MemberVO memberVO : memberVOList) {
            String convertedName = gernerateNewMemberNameIfDuplicated(memberVO.getName());
            memberVO.setName(convertedName);
            memberService.createMemberFromMemberVO(memberVO);
        }
    }

    private String gernerateNewMemberNameIfDuplicated(String name) {
        long nameDuplicationCount = countNameDuplication(name);
        return NameGenerator.generateNameWhenDuplication(name, nameDuplicationCount);
    }

    private Long countNameDuplication(String name) {
        return memberService.countMemberNameDuplication(name);
    }

    private List<MemberVO> convertCSVMemberVOListToMemberVOList(List<CSVMemberVO> csvMemberVOList) {
        List<MemberVO> convertedList = new ArrayList<>();

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {
            MemberVO converted = convertCSVMemberVOToMemberVO(csvMemberVO);
            convertedList.add(converted);
        }
        return convertedList;
    }

    private MemberVO convertCSVMemberVOToMemberVO(CSVMemberVO csvMemberVO) {
        return MemberVO
                .builder()
                .id(csvMemberVO.getId())
                .name(csvMemberVO.getName())
                .businessCall(csvMemberVO.getBusinessCall())
                .cellPhone(csvMemberVO.getCellPhone())
                .position(csvMemberVO.getPosition())
                .duty(csvMemberVO.getDuty())
                .build();
    }

    private List<TeamVO> extractTeamVOListFromCSVMemberVOList(List<CSVMemberVO> csvMemberVOList) {
        List<TeamVO> teamVOList = new ArrayList<>();

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {
            TeamVO teamVO = extractTeamVOFromCSVMemberVO(csvMemberVO);
            teamVOList.add(teamVO);
        }

        return teamVOList;
    }

    private void createTeamsFromTeamVOList(List<TeamVO> teamVOList) {
        for (TeamVO teamVO : teamVOList) {
            if (checkTeamNameDuplication(teamVO.getName())) {
                continue;
            }
            teamService.createTeamWhenTeamNameNotDuplicated(teamVO);
        }
    }

    private boolean checkTeamNameDuplication(String teamName) {
        return teamService.checkExistWithTeamName(teamName);
    }
}
