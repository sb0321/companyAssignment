package com.assign.organization.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberVO {

    private Long id;
    private String teamName;
    private String name;
    private String cellPhone;
    private String businessCall;
    private String position;
    private String duty;

    @Builder
    public MemberVO(Long id, String teamName, String name, String cellPhone, String businessCall, String position, String duty) {
        this.id = id;
        this.teamName = teamName;
        this.name = name;
        this.cellPhone = cellPhone;
        this.businessCall = businessCall;
        this.position = position;
        this.duty = duty;
    }
}
