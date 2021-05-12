package com.assign.organization.controller.main.responsedomain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CSVStatusResponse {

    public enum ResponseStatus {
        OK, FAIL
    }

    private final ResponseStatus status;
    private final List<String> messages;
}
