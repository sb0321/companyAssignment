package com.assign.organization.domain.member;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberVO {

    private Long id;
    private String name;
    private String cellPhone;
    private String businessCall;
    private String position;
    private String duty;

    @Builder
    public MemberVO(Long id, String name, String cellPhone, String businessCall, String position, String duty) {
        this.id = id;
        this.name = name;
        this.cellPhone = cellPhone;
        this.businessCall = businessCall;
        this.position = position;
        this.duty = duty;
    }
}
