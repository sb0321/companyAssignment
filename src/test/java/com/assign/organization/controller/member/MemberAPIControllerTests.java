package com.assign.organization.controller.member;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.utils.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class MemberAPIControllerTests {

    @Value("${csv.data}")
    private String csvPath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Test
    @Transactional
    public void testSearchKeywordMemberVOList() throws Exception {

        memberService.insertMembersFromCSVFile(csvPath);

        MvcResult mvcResult = mockMvc.perform(get("/member")
                .param("keyword", "승빈"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        log.info(mvcResult.getRequest().getContentAsString());
    }

}