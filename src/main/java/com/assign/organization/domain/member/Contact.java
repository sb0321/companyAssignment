package com.assign.organization.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
public class Contact {

    private String cellPhone;
    private String businessCall;

    @Builder
    public Contact(String cellPhone, String businessCall) {
        this.cellPhone = cellPhone;
        this.businessCall = businessCall;
    }
}
