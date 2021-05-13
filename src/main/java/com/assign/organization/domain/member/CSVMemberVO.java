package com.assign.organization.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
public class CSVMemberVO {

    private final Long memberId;
    private final LocalDate enteredDate;
    private final String name;
    private final String teamName;
    private final String businessCall;
    private final String cellPhone;
    private final String duty;
    private final String position;
}
