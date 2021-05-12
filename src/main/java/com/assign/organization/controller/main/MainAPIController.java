package com.assign.organization.controller.main;

import com.assign.organization.controller.main.responsedomain.CSVStatusResponse;
import com.assign.organization.exception.InvalidCSVFileException;
import com.assign.organization.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainAPIController {

    private static final String SUCCESS_MESSAGE = "CSV파일로부터 동기화를 완료했습니다.";

    private final MemberService memberService;

    @GetMapping("/read")
    public ResponseEntity<CSVStatusResponse> init(@RequestParam(value = "csvFilePath") String csvFilePath)
            throws InvalidCSVFileException {
        memberService.insertMembersFromCSVFile(csvFilePath);

        List<String> messages = new LinkedList<>();
        messages.add(SUCCESS_MESSAGE);

        CSVStatusResponse response = new CSVStatusResponse(CSVStatusResponse.ResponseStatus.OK, messages);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
