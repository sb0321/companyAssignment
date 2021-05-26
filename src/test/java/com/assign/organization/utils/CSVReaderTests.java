package com.assign.organization.utils;

import com.assign.organization.exception.CSVFileFormatException;
import com.assign.organization.exception.InvalidFilePathException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.BufferedReader;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class CSVReaderTests {

    CSVReader csvReader = new CSVReader();

    String csvSuccessPath;
    String csvFileException;
    String csvExtensionFailPath;
    String csvExtensionNotExistPath;

    @BeforeAll
    void init() {
        csvSuccessPath = new File("src/test/resources/data/data.csv").getAbsolutePath();
        csvFileException = new File("/src/test/resources/thereisnothing.csv").getAbsolutePath();
        csvExtensionFailPath = new File("src/test/resources/data/data.file").getAbsolutePath();
        csvExtensionNotExistPath = new File("src/test/resources/data").getAbsolutePath();
    }

    @Test
    void testGetBufferedReaderFromCSVFilePath() {
        BufferedReader bufferedReader = whenGetBufferedReader(csvSuccessPath);
        thenGetBufferedReaderSuccess(bufferedReader);

        Exception exception = whenGetBufferedReaderThrowsException(csvFileException);
        thenGetBufferedReaderThrowsInvalidFilePathException(exception);

        exception = whenGetBufferedReaderThrowsException(csvExtensionFailPath);
        thenGetBufferedReaderThrowsCSVFileFormatException(exception);

        exception = whenGetBufferedReaderThrowsException(csvExtensionNotExistPath);
        thenGetBufferedReaderThrowsCSVFileFormatException(exception);

    }

    BufferedReader whenGetBufferedReader(String csvFilePath) {
        return csvReader.getBufferedReaderFromCSVFilePath(csvFilePath);
    }

    void thenGetBufferedReaderSuccess(BufferedReader bufferedReader) {
        assertNotNull(bufferedReader);
    }

    Exception whenGetBufferedReaderThrowsException(String csvFilePath) {
        return assertThrows(Exception.class, () -> csvReader.getBufferedReaderFromCSVFilePath(csvFilePath));
    }

    void thenGetBufferedReaderThrowsInvalidFilePathException(Exception e) {
        assertEquals(InvalidFilePathException.class, e.getClass());
    }

    void thenGetBufferedReaderThrowsCSVFileFormatException(Exception e) {
        assertEquals(CSVFileFormatException.class, e.getClass());
    }

}