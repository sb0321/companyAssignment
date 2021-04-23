package com.assign.organization.controller.member;

import com.assign.organization.domain.member.Address;
import com.assign.organization.domain.member.MemberDTO;
import com.assign.organization.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void testCreateMember() throws Exception {

        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("name", "testName");
        params.add("cellPhone", "010-1111-1111");
        params.add("businessCall", "1101");
        params.add("ranked", "팀장");

        Address address = Address
                .builder()
                .cellPhone("010-1111-1111")
                .businessCall("1011")
                .build();

        MemberDTO dto = MemberDTO
                .builder()
                .name("testName")
                .address(address)
                .id(1L)
                .duty("팀장")
                .build();

        Mockito.when(memberService.createMember(any())).thenReturn(dto);

        // when
        MvcResult mvcResult = mockMvc.perform(post("/member")
                .params(params))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        log.info(mvcResult.toString());

        String memberId = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(1L, Long.parseLong(memberId));
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

    @Test
    public void testGetMember() throws Exception {

        // given
        Address address = Address
                .builder()
                .businessCall("1111")
                .cellPhone("010-1111-1111")
                .build();

        MemberDTO dto = MemberDTO
                .builder()
                .id(1L)
                .position("인턴")
                .address(null)
                .duty("팀장")
                .teamId(2L)
                .name("testMember")
                .address(address)
                .build();

        Mockito.when(memberService.findMemberById(1L)).thenReturn(dto);

        MvcResult mvcResult = mockMvc.perform(get("/member/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        log.info(mvcResult.getResponse().getContentAsString());
    }
}