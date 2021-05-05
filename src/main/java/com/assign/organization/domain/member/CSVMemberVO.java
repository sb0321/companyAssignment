package com.assign.organization.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CSVMemberVO {

    private final String name;
    private final String teamName;
    private final String businessCall;
    private final String cellPhone;
    private final String duty;
    private final String position;
}
