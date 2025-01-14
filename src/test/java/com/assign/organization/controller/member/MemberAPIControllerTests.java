package com.assign.organization.controller.member;

import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.exception.CSVReaderException;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.service.team.TeamService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:application.properties")
class MemberAPIControllerTests {

    static final String UTF8_CHARSET = "UTF-8";

    MockMvc mockMvc;

    @Autowired
    MemberAPIController controller;

    @Autowired
    MemberService memberService;

    @Autowired
    TeamService teamService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    void init() throws CSVReaderException {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilter(new CharacterEncodingFilter(UTF8_CHARSET, true))
                .build();

        teamService.insertTeamsFromDataPath(new File("src/test/resources/data/data.csv").getAbsolutePath());
    }

    @AfterAll
    void purge() {
        memberRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"승빈", "1000", "웹개발 1팀"})
    void testSearchKeywordMemberVOList(String keyword) throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/member")
                .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print())
                .andReturn();

        List<MemberVO> memberVOList = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<MemberVO>>() {
                });

        List<MemberVO> expected = memberService.findMembersContainsKeyword(keyword);

        assertTrue(Objects.deepEquals(expected, memberVOList));
    }

    @ParameterizedTest
    @NullSource
    void testSearchKeywordMemberVOListNull(String nullKeyword) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/member")
                .param("keyword", nullKeyword))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();

        List<MemberVO> memberVOList = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<MemberVO>>() {});

        List<MemberVO> expected = memberService.findMembersContainsKeyword("");

        assertTrue(Objects.deepEquals(expected, memberVOList));
    }
}