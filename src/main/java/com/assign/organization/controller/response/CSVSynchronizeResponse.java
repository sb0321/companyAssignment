package com.assign.organization.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CSVSynchronizeResponse extends SimpleResponse {

    private String message;

    public CSVSynchronizeResponse(ResponseStatus status, String message) {
        super(status);
        this.message = message;
    }
}
