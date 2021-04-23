package com.assign.organization.controller.team;

import com.assign.organization.domain.member.Address;
import com.assign.organization.domain.member.MemberDTO;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamDTO;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.team.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class TeamAPIControllerTests {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamAPIController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void testCreateTeam() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("name", "testTeam");

        Mockito.when(teamService.createTeam(any())).thenReturn(new Team());

        // when
        mockMvc.perform(post("/team")
                .params(params))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getTeam() throws Exception {

        // given
        String teamName = "testTeam";

        Address teamLeaderAddress = Address
                .builder()
                .businessCall("1111")
                .cellPhone("010-1111-1111")
                .build();

        MemberDTO teamLeader = MemberDTO
                .builder()
                .teamId(1L)
                .address(teamLeaderAddress)
                .name("teamLeader")
                .position("사장")
                .duty("팀장")
                .build();

        Set<MemberDTO> teamMembers = new HashSet<>();

        for (int memberIndex = 0; memberIndex < 10; memberIndex++) {

            Address memberAddress = Address
                    .builder()
                    .businessCall(memberIndex + "000")
                    .cellPhone("010-1111-000" + memberIndex)
                    .build();

            MemberDTO m = MemberDTO
                    .builder()
                    .name(String.valueOf(memberIndex))
                    .address(memberAddress)
                    .position("인턴")
                    .build();

            teamMembers.add(m);
        }


        TeamDTO teamDTO = TeamDTO
                .builder()
                .teamLeader(teamLeader)
                .members(teamMembers)
                .name("testTeam")
                .build();

        Mockito.when(teamService.findTeamById(any())).thenReturn(teamDTO);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/team/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultJson = mvcResult.getResponse().getContentAsString();

        TeamVO returnedVO = new ObjectMapper().readValue(resultJson, TeamVO.class);

        log.info(returnedVO.toString());

        // then
        assertEquals(teamDTO.getName(), returnedVO.getName());
        assertEquals(teamDTO.getTeamLeader().getName(), returnedVO.getTeamLeader().getName());
        assertEquals(teamDTO.getMembers().size(), returnedVO.getMembers().size());

    }

}