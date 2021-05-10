package com.assign.organization.domain.member;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {

    private Long id;
    private String teamName;
    private String name;
    private String cellPhone;
    private String businessCall;
    private String position;
    private String duty;
}
