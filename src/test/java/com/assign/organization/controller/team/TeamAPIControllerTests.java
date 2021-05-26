package com.assign.organization.controller.team;

import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.team.TeamService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:application.properties")
class TeamAPIControllerTests {

    static final String CHARSET = "UTF-8";

    MockMvc mockMvc;

    @Autowired
    TeamAPIController teamAPIController;

    @Autowired
    TeamService teamService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    void init() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(teamAPIController)
                .addFilters(new CharacterEncodingFilter(CHARSET, true))
                .build();

        teamService.insertTeamsFromDataPath(new File("src/test/resources/data/data.csv").getAbsolutePath());
    }

    @Test
    void testGetTeamsWithMembers() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/team"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<TeamVO> list = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<TeamVO>>() {
                });

        List<TeamVO> expected = teamService.findAllTeamVOList();

        assertTrue(Objects.deepEquals(expected, list));

        log.info(list.toString());
    }
}