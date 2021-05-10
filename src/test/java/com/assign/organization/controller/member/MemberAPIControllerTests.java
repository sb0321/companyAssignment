package com.assign.organization.controller.member;

import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.exception.CSVFileInvalidException;
import com.assign.organization.service.member.MemberService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
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

    @Value(value = "${csv.data}")
    String CSV_FILE_PATH;

    @BeforeAll
    void init() throws CSVFileInvalidException {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilter(new CharacterEncodingFilter(UTF8_CHARSET, true))
                .build();

        memberService.insertMembersFromCSVFile(CSV_FILE_PATH);
    }

    @ParameterizedTest
    @ValueSource(strings = {"승빈", "1000", "웹개발 1팀", ""})
    void testSearchKeywordMemberVOList(String keyword) throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/member")
                .param("keyword", keyword))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<MemberVO> memberVOList = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<MemberVO>>() {});

        List<MemberVO> expected = memberService.findMembersContainsKeyword(keyword);

        assertTrue(Objects.deepEquals(expected, memberVOList));
    }

}