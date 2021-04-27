package com.assign.organization.controller.team;

import com.assign.organization.domain.team.Team;
import com.assign.organization.service.team.TeamService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.ArgumentMatchers.any;
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

}