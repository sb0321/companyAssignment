package com.assign.organization.controller.main;

import com.assign.organization.controller.response.CSVSynchronizeResponse;
import com.assign.organization.controller.response.SimpleResponse;
import com.assign.organization.domain.duplication.Duplication;
import com.assign.organization.domain.duplication.repository.DuplicationRepository;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
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
    DuplicationRepository duplicationRepository;

    @Autowired
    ObjectMapper objectMapper;

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

        String csvFileSuccess = new File("src/test/resources/data/data.csv").getAbsolutePath();

        assertDoesNotThrow(() -> {
            MvcResult mvcResult = mockMvc.perform(
                    get("/read")
                            .param("csvFilePath", csvFileSuccess)
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
            assertEquals("파일의 경로가 잘못되었습니다", exceptionMessage);
        });
    }

    @Test
    void testReadPathNull() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/read"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        });
    }

    @Test
    void testReadThrowsException() {

        String csvFileSuccess = new File("src/test/resources/data/data.csv").getAbsolutePath();

        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/read")
                    .param("csvFilePath", csvFileSuccess)
            )
                    .andExpect(status().isOk());

            MvcResult mvcResult = mockMvc.perform(get("/read")
                    .param("csvFilePath", csvFileSuccess)
            )
                    .andExpect(status().isBadRequest())
                    .andReturn();

            String result = mvcResult.getResponse().getContentAsString();
            log.info(result);
            CSVSynchronizeResponse response = objectMapper.readValue(result, CSVSynchronizeResponse.class);

            String exceptionMessage = response.getMessage();
            exceptionMessage = exceptionMessage.substring(0, exceptionMessage.indexOf(":"));

            assertEquals(SimpleResponse.ResponseStatus.FAIL, response.getStatus());
            assertEquals("중복되거나 null값인 항목이 있습니다 ", exceptionMessage);
        });
    }

    @Test
    void ee() {
        String csvFileBusinessCallDuplication =
                new File("src/test/resources/data/businessCallDuplication.csv")
                        .getAbsolutePath();

        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/read")
                    .param("csvFilePath", csvFileBusinessCallDuplication)
            )
                    .andExpect(status().isBadRequest())
                    .andReturn();

            Optional<Duplication> findDuplication = duplicationRepository.findById("1000");
            assertTrue(findDuplication.isPresent());
        });
    }

}