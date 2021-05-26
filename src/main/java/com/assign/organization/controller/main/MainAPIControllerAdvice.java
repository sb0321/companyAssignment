package com.assign.organization.controller.main;

import com.assign.organization.controller.response.CSVSynchronizeResponse;
import com.assign.organization.controller.response.SimpleResponse;
import com.assign.organization.exception.CSVReaderException;
import com.assign.organization.exception.InvalidMemberException;
import com.assign.organization.exception.NullCSVFilePathException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = {"com.assign.organization.controller.main"})
public class MainAPIControllerAdvice {

    @ExceptionHandler(CSVReaderException.class)
    public ResponseEntity<CSVSynchronizeResponse> handleCsvFileException(CSVReaderException e) {

        CSVSynchronizeResponse response = new CSVSynchronizeResponse(SimpleResponse.ResponseStatus.FAIL, e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NullCSVFilePathException.class)
    public ResponseEntity<CSVSynchronizeResponse> handleCsvFileException(NullCSVFilePathException e) {
        CSVSynchronizeResponse response = new CSVSynchronizeResponse(SimpleResponse.ResponseStatus.FAIL, e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidMemberException.class)
    public ResponseEntity<CSVSynchronizeResponse> handleInvalidMemberException(InvalidMemberException e) {
        CSVSynchronizeResponse response = new CSVSynchronizeResponse(SimpleResponse.ResponseStatus.FAIL, e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

}
