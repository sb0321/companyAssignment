package com.assign.organization.controller.main;

import com.assign.organization.controller.response.CSVSynchronizeResponse;
import com.assign.organization.controller.response.SimpleResponse;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.repository.TeamRepository;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    ObjectMapper objectMapper;

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
    void after() {
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
                    .andExpect(status().isOk())
                    .andReturn();

            String result = mvcResult.getResponse().getContentAsString();
            CSVSynchronizeResponse response = objectMapper.readValue(result, CSVSynchronizeResponse.class);

            assertEquals(SimpleResponse.ResponseStatus.OK, response.getStatus());
            assertEquals("CSV파일로부터 동기화를 완료했습니다.", response.getMessage());
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
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String result = mvcResult.getResponse().getContentAsString();
            CSVSynchronizeResponse response = objectMapper.readValue(result, CSVSynchronizeResponse.class);

            String exceptionMessage = response.getMessage();
            exceptionMessage = exceptionMessage.substring(0, exceptionMessage.indexOf("."));

            assertEquals(SimpleResponse.ResponseStatus.FAIL, response.getStatus());
            assertEquals("파일 경로가 잘못되어 있습니다", exceptionMessage);
        });
    }

    @Test
    void testReadPathNull() {
        assertDoesNotThrow(() -> {
            MvcResult mvcResult = mockMvc.perform(get("/read"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String result = mvcResult.getResponse().getContentAsString();
            CSVSynchronizeResponse response = objectMapper.readValue(result, CSVSynchronizeResponse.class);

            assertEquals("CSV파일 경로 파라미터가 없습니다.", response.getMessage());
            assertEquals(SimpleResponse.ResponseStatus.FAIL, response.getStatus());
        });
    }

    @Test
    void testReadMemberIdDuplication() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/read")
                    .param("csvFilePath", CSV_FILE_SUCCESS)
            )
                    .andExpect(status().isOk());

            MvcResult mvcResult = mockMvc.perform(get("/read")
                    .param("csvFilePath", CSV_FILE_SUCCESS)
            )
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String result = mvcResult.getResponse().getContentAsString();
            CSVSynchronizeResponse response = objectMapper.readValue(result, CSVSynchronizeResponse.class);

            String exceptionMessage = response.getMessage();
            exceptionMessage = exceptionMessage.substring(0, exceptionMessage.indexOf("."));

            assertEquals(SimpleResponse.ResponseStatus.FAIL, response.getStatus());
            assertEquals("중복되는 사번이 있습니다", exceptionMessage);
        });
    }

}