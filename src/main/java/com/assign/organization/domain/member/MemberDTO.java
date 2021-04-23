package com.assign.organization.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberDTO {

    private Long id;
    private String name;
    private Long teamId;
    private String position;
    private String duty;
    private Address address;

    @Builder
    public MemberDTO(Long id, String name, Long teamId, String position, String duty, Address address) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
        this.position = position;
        this.duty = duty;
        this.address = address;
    }
}
