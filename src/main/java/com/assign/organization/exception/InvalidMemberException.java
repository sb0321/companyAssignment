package com.assign.organization.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidMemberException extends RuntimeException {
    public InvalidMemberException(String message) {
        super(message);
    }
}
