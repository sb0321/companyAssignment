package com.assign.organization.controller.member;

import com.assign.organization.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith({MockitoExtension.class})
class MemberAPIControllerTests {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberAPIController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void testUpdateMember() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("name", "newMember");
        params.add("cellPhone", "010-2222-2222");
        params.add("businessCall", "0000");
        params.add("ranked", "팀장");

        mockMvc.perform(put("/member/1")
                .params(params))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteMember() throws Exception {

        mockMvc.perform(delete("/member/1"))
                .andExpect(status().isOk())
                .andDo(print());

    }
}