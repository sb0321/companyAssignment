package com.assign.organization.controller.member;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.service.team.TeamService;
import com.assign.organization.utils.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class MemberAPIControllerTests {

    @Value("${csv.data}")
    private String csvPath;

    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MemberAPIController controller;

    @BeforeAll
    public void init() throws IOException {

        List<CSVMemberVO> csvMemberVOList = CSVReader.readCSVFile(csvPath);
        memberService.insertMembersFromCSVMemberVOList(csvMemberVOList);

        List<TeamVO> teamVOList = teamService.extractTeamVOListFromCSVMemberVOList(csvMemberVOList);
        teamService.insertTeamsFromTeamVOList(teamVOList);

        makeConnectionBetweenMemberAndTeamWithList(csvMemberVOList);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .addFilter(new CharacterEncodingFilter("UTF-8"))
                .build();
    }

    private void makeConnectionBetweenMemberAndTeamWithList(List<CSVMemberVO> csvMemberVOList) {
        for (CSVMemberVO csvMemberVO : csvMemberVOList) {
            Member findMember = memberService.findMemberById(csvMemberVO.getId());
            Team findTeam = teamService.findTeamByTeamName(csvMemberVO.getTeamName());

            teamService.addMemberToTeam(findTeam, findMember);
        }
    }

    @Test
    public void testSearchKeywordMemberVOList() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/member?keyword=승빈"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

}