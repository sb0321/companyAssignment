package com.assign.organization.controller.main;

import com.assign.organization.controller.response.CSVSynchronizeResponse;
import com.assign.organization.exception.InvalidCSVFileException;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.service.team.TeamService;
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
    private static final String FILE_DOES_NOT_EXIST_MESSAGE = "CSV파일 경로 파라미터가 없습니다.";

    private final TeamService teamService;

    @GetMapping("/read")
    public ResponseEntity<CSVSynchronizeResponse> read(
            @RequestParam(value = "csvFilePath", required = false) String csvFilePath)
            throws InvalidCSVFileException {

        if (csvFilePath == null) {
            throw new InvalidCSVFileException(FILE_DOES_NOT_EXIST_MESSAGE);
        }

        teamService.insertMembersFromCSVFile(csvFilePath);
        CSVSynchronizeResponse response = new CSVSynchronizeResponse(CSVSynchronizeResponse.ResponseStatus.OK, SUCCESS_MESSAGE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
