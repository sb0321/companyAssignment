package com.assign.organization.domain.member;

import com.assign.organization.exception.NullMemberException;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
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
    private LocalDate enteredDate;

    public static MemberVO from(Member member) {

        if (member == null) {
            throw new NullMemberException();
        }

        return MemberVO
                .builder()
                .id(member.getId())
                .position(member.getPosition())
                .businessCall(member.getBusinessCall())
                .cellPhone(member.getCellPhone())
                .teamName(member.getTeam() != null ? member.getTeam().getName() : null)
                .duty(member.getDuty())
                .name(member.getFullName())
                .build();
    }
}
