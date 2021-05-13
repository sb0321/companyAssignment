package com.assign.organization.controller.main.responsedomain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CSVSynchronizeResponse {

    public enum ResponseStatus {
        OK, FAIL
    }

    private ResponseStatus status;
    private String message;
}
