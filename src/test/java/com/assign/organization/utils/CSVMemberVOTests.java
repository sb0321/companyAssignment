package com.assign.organization.utils;

import com.assign.organization.exception.InvalidCSVFileException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CSVMemberVOTests {


    @ParameterizedTest
    @ValueSource(strings = "1,1,홍 길동,2021-05-24,1000,010-0000-0000,웹개발 1팀,차장,팀장")
    void testCSVMemberVOInsertSuccess(String rawMemberData) {
        assertDoesNotThrow(() -> {
            CSVMemberVO vo = CSVMemberVO.from(rawMemberData);

            assertEquals(1L, vo.getMemberId());
            assertEquals("홍", vo.getLastName());
            assertEquals("길동", vo.getFirstName());
            assertEquals(LocalDate.parse("2021-05-24"), vo.getEnteredDate());
            assertEquals("1000", vo.getBusinessCall());
            assertEquals("010-0000-0000", vo.getCellPhone());
            assertEquals("웹개발 1팀", vo.getTeamName());
            assertEquals("차장", vo.getPosition());
            assertEquals("팀장", vo.getDuty());
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"1,1,홍길동,2021-05-24,1000,010-0000-0000,웹개발 1팀,차장,팀장"})
    void testCSVMemberVOInsertFail(String rawMemberData) {
        InvalidCSVFileException exception =
                assertThrows(InvalidCSVFileException.class, () -> CSVMemberVO.from(rawMemberData));
        log.info(exception.getMessage());
    }

}