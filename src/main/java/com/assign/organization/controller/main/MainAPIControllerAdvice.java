package com.assign.organization.controller.main;

import com.assign.organization.controller.main.responsedomain.CSVStatusResponse;
import com.assign.organization.exception.InvalidCSVFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestControllerAdvice(basePackages = {"com.assign.organization.controller.main"})
public class MainAPIControllerAdvice {

    @ExceptionHandler(InvalidCSVFileException.class)
    public ResponseEntity<CSVStatusResponse> handleCsvFileException(InvalidCSVFileException e) {

        List<String> messages = new LinkedList<>();
        messages.add(e.getMessage());

        CSVStatusResponse response = new CSVStatusResponse(CSVStatusResponse.ResponseStatus.FAIL, messages);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
