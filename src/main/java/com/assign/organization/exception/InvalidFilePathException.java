package com.assign.organization.exception;

public class InvalidFilePathException extends CSVFileFormatException {
    public InvalidFilePathException(String message) {
        super(message);
    }
}
