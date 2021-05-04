package com.assign.organization.domain.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class CSVMemberVO {

    private Long id;
    private String name;
    private String teamName;
    private String businessCall;
    private String cellPhone;
    private String duty;
    private String position;
}
