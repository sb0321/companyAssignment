package com.assign.organization.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleResponse {

    public enum ResponseStatus {
        OK, FAIL
    }

    private ResponseStatus status;
}
