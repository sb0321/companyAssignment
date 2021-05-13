package com.assign.organization.controller.main;

import com.assign.organization.controller.main.response.CSVSynchronizeResponse;
import com.assign.organization.exception.InvalidCSVFileException;
import com.assign.organization.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainAPIController {

    private static final String SUCCESS_MESSAGE = "CSV파일로부터 동기화를 완료했습니다.";

    private final MemberService memberService;

    @GetMapping("/read")
    public ResponseEntity<CSVSynchronizeResponse> read(
            @RequestParam(value = "csvFilePath", defaultValue = "") String csvFilePath)
            throws InvalidCSVFileException {

        memberService.insertMembersFromCSVFile(csvFilePath);
        CSVSynchronizeResponse response = new CSVSynchronizeResponse(CSVSynchronizeResponse.ResponseStatus.OK, SUCCESS_MESSAGE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
