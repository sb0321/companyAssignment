package com.assign.organization.controller.main;

import com.assign.organization.controller.response.CSVSynchronizeResponse;
import com.assign.organization.controller.response.SimpleResponse;
import com.assign.organization.exception.CSVReaderException;
import com.assign.organization.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainAPIController {

    private static final String SUCCESS_MESSAGE = "CSV파일로부터 동기화를 완료했습니다.";

    private final TeamService teamService;

    @GetMapping("/read")
    public ResponseEntity<CSVSynchronizeResponse> read(
            @RequestParam(value = "csvFilePath", required = false) String csvFilePath) {

        teamService.insertTeamsFromDataPath(csvFilePath);
        CSVSynchronizeResponse response = new CSVSynchronizeResponse(SimpleResponse.ResponseStatus.OK, SUCCESS_MESSAGE);
        return ResponseEntity.of(Optional.of(response));
    }

}
