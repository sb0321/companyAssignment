package com.assign.organization.controller.main;

import com.assign.organization.controller.response.CSVSynchronizeResponse;
import com.assign.organization.exception.InvalidCSVFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = {"com.assign.organization.controller.main"})
public class MainAPIControllerAdvice {

    @ExceptionHandler(InvalidCSVFileException.class)
    public ResponseEntity<CSVSynchronizeResponse> handleCsvFileException(InvalidCSVFileException e) {

        CSVSynchronizeResponse response = new CSVSynchronizeResponse(CSVSynchronizeResponse.ResponseStatus.FAIL, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
