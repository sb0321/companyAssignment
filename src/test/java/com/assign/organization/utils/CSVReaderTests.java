package com.assign.organization.utils;

import com.assign.organization.exception.CSVFileInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application.properties")
class CSVReaderTests {

    @Value(value = "${csv.data.success}")
    String CSV_FILE_OK_PATH;

    @Value(value = "${csv.data.fail}")
    String CSV_FILE_FAIL_PATH;

    @Test
    void testReadCSVFile() {

        assertThrows(CSVFileInvalidException.class, () -> CSVReader.readCSVFile(CSV_FILE_FAIL_PATH));

//        assertThrows(CSVFileInvalidException.class, () -> CSVReader.readCSVFile("falsePath"));
//
//        assertDoesNotThrow(() -> {
//            CSVReader.readCSVFile(CSV_FILE_OK_PATH);
//        });

    }
}