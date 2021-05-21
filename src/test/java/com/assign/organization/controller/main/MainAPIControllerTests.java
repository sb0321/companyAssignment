package com.assign.organization.controller.main;

import com.assign.organization.controller.response.CSVSynchronizeResponse;
import com.assign.organization.controller.response.SimpleResponse;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.exception.InvalidCSVFileException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Slf4j
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MainAPIControllerTests {


    MockMvc mockMvc;

    @Autowired
    MainAPIController controller;

    @Autowired
    MainAPIControllerAdvice controllerAdvice;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Value(value = "${csv.data.success}")
    String CSV_FILE_SUCCESS;

    @BeforeAll
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(controllerAdvice)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @AfterEach
    void purge() {
        memberRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    void testReadSuccess() {
        assertDoesNotThrow(() -> {
            MvcResult mvcResult = mockMvc.perform(
                    get("/read")
                            .param("csvFilePath", CSV_FILE_SUCCESS)
            )
                    .andDo(print())
                    .andReturn();

            String result = mvcResult.getResponse().getContentAsString();
            CSVSynchronizeResponse response = new ObjectMapper().readValue(result, CSVSynchronizeResponse.class);

            assertEquals(CSVSynchronizeResponse.ResponseStatus.OK, response.getStatus());
            log.info(response.getMessage());
        });
    }

    @Test
    void testReadPathFail() {
        assertDoesNotThrow(() -> {
            MvcResult mvcResult = mockMvc.perform(
                    get("/read")
                            .param("csvFilePath", "failedPath.csv")
            )
                    .andDo(print())
                    .andReturn();

            String result = mvcResult.getResponse().getContentAsString();
            CSVSynchronizeResponse response = new ObjectMapper().readValue(result, CSVSynchronizeResponse.class);

            assertEquals(CSVSynchronizeResponse.ResponseStatus.FAIL, response.getStatus());
            log.info(response.getMessage());
        });
    }

    @Test
    void testReadMemberIdDuplication() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/read")
                    .param("csvFilePath", CSV_FILE_SUCCESS));

            MvcResult mvcResult = mockMvc.perform(get("/read")
                    .param("csvFilePath", CSV_FILE_SUCCESS))
                    .andReturn();

            String result = mvcResult.getResponse().getContentAsString();
            CSVSynchronizeResponse response = new ObjectMapper().readValue(result, CSVSynchronizeResponse.class);
            
            String exceptionMessage = response.getMessage();
            exceptionMessage = exceptionMessage.substring(0, exceptionMessage.indexOf("."));
            assertEquals("중복되는 사번이 있습니다", exceptionMessage);
        });
    }

}