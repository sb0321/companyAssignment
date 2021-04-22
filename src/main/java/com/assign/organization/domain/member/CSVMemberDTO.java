package com.assign.organization.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CSVMemberDTO {

    private Long id;
    private String name;
    private String teamName;
    private String businessCall;
    private String cellPhone;
    private String duty;
    private String position;

    @Builder
    public CSVMemberDTO(Long id, String name, String teamName, String businessCall, String cellPhone, String duty, String position) {
        this.id = id;
        this.name = name;
        this.teamName = teamName;
        this.businessCall = businessCall;
        this.cellPhone = cellPhone;
        this.duty = duty;
        this.position = position;
    }
}
