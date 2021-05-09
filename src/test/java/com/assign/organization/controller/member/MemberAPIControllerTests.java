package com.assign.organization.controller.member;

import com.assign.organization.exception.CSVFileInvalidException;
import com.assign.organization.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

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

    @Test
    void testSearchKeywordMemberVOList() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/member")
                .param("keyword", "승빈"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        log.info(mvcResult.getRequest().getContentAsString());
    }

}