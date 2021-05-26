package com.assign.organization.utils;

import com.assign.organization.domain.member.Nationality;
import com.assign.organization.exception.CSVRawDataConvertException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CSVMemberVOTests {

    @Test
    void testFrom() {
        String failRawMemberData = "1, 1, 송 태근, 2021-05-333, 1000, 010-0000-0000, 웹개발 1팀, 차장, 팀장, KOREA";
        thenFromThrowsCSVRawDataConvertException(failRawMemberData);

        String rawMemberData = "1,1,홍 길동,2021-05-24,1000,010-0000-0000,웹개발 1팀,차장,팀장,KOREA";
        thenFromSuccess(rawMemberData);
    }

    void thenFromThrowsCSVRawDataConvertException(String rawMemberData) {
        assertThrows(CSVRawDataConvertException.class, () -> CSVMemberVO.from(rawMemberData));
    }

    void thenFromSuccess(String rawMemberData) {
        assertDoesNotThrow(() -> {
            CSVMemberVO vo = CSVMemberVO.from(rawMemberData);

            String[] splitData = rawMemberData.split(",");

            String firstName, lastName;
            String[] splitName = splitData[2].split(" ");
            if (splitData[9].equals(Nationality.ENGLISH)) {
                firstName = splitName[0];
                lastName = splitName[1];
            } else {
                lastName = splitName[0];
                firstName = splitName[1];
            }

            assertEquals(Long.parseLong(splitData[1]), vo.getMemberId());
            assertEquals(lastName, vo.getLastName());
            assertEquals(firstName, vo.getFirstName());
            assertEquals(LocalDate.parse(splitData[3]), vo.getEnteredDate());
            assertEquals(splitData[4], vo.getBusinessCall());
            assertEquals(splitData[5], vo.getCellPhone());
            assertEquals(splitData[6], vo.getTeamName());
            assertEquals(splitData[7], vo.getPosition());
            assertEquals(splitData[8], vo.getDuty());
            assertEquals(Nationality.valueOf(splitData[9]), vo.getNationality());
        });
    }

}