package com.assign.organization.domain.member;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Address {

    private String cellPhone;
    private String businessCall;

    @Builder
    public Address(String cellPhone, String businessCall) {
        this.cellPhone = cellPhone;
        this.businessCall = businessCall;
    }
}
