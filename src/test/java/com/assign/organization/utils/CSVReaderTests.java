package com.assign.organization.utils;

import com.assign.organization.exception.InvalidCSVFileException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application.properties")
class CSVReaderTests {

    @Value(value = "${csv.data.success}")
    String CSV_FILE_OK_PATH;

    @Value(value = "${csv.data.fail}")
    String CSV_FILE_FAIL_PATH;

    @Value(value = "${csv.data.invalid.extension}")
    String INVALID_CSV_FILE_PATH;

    @AfterEach
    void close() throws IOException {
        CSVReader.close();
    }


    @Test
    void testReadCSVFileSuccess() {
        assertDoesNotThrow(() -> {
            CSVReader.setCSVFile(CSV_FILE_OK_PATH);
            CSVReader.readCSVMemberVOList(1000);
        });
    }

    @Test
    void testReadCSVFileFailWithInvalidPath() {
        assertThrows(InvalidCSVFileException.class, () -> {
            CSVReader.setCSVFile("failedPath.csv");
            CSVReader.readCSVMemberVOList(1000);
        });
    }

    @Test
    void testReadCSVFileFailWithInvalidFile() {
        InvalidCSVFileException exception =
                assertThrows(InvalidCSVFileException.class, () -> {
                    CSVReader.setCSVFile(CSV_FILE_FAIL_PATH);
                    CSVReader.readCSVMemberVOList(1000);
                });

        String exceptionMessage = exception.getMessage();
        exceptionMessage = exceptionMessage.substring(0, exceptionMessage.indexOf(":"));
        assertEquals("raw 데이터를 변환하는데 실패했습니다", exceptionMessage.trim());
    }

    @Test
    void testReadCSVFileFailWithInvalidExtension() {
        assertThrows(InvalidCSVFileException.class, () -> CSVReader.setCSVFile(INVALID_CSV_FILE_PATH));
    }

    @Test
    void testReadCSVMemberVOListWithoutInitialize() {
        InvalidCSVFileException exception =
                assertThrows(InvalidCSVFileException.class, () -> CSVReader.readCSVMemberVOList(100));

        assertEquals("CSVReader를 초기화 하지 않았습니다", exception.getMessage());
    }
}