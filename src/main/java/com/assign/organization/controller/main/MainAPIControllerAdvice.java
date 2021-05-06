package com.assign.organization.controller.main;

import com.assign.organization.exception.CSVFileInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MainAPIControllerAdvice {

    @ExceptionHandler(CSVFileInvalidException.class)
    public String csvFileException() {
        return "csv file is not valid";
    }

}
