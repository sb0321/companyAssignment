package com.assign.organization.utils;

import com.assign.organization.domain.member.CSVMemberVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@ExtendWith({SpringExtension.class})
@TestPropertySource(value = "classpath:application.properties")
class CSVReaderTests {

    @Value("${csv.data}")
    private String CSV_FILE_PATH;

    @Test
    void testReadCSVFile() throws IOException {

        log.info(CSV_FILE_PATH);

        List<CSVMemberVO> csvMemberVOList = CSVReader.readCSVFile(CSV_FILE_PATH);

        log.info(csvMemberVOList.toString());

        // fail
        try {
            List<CSVMemberVO> failed = CSVReader.readCSVFile("failedPath");
            fail("파일이 존재하지 않는데 IOException이 발생하지 않았습니다.");
        } catch (IOException e) {

        }
    }

}