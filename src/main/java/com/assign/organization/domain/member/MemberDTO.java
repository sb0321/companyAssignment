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
    private Position position;
    private String ranked;
    private Address address;

    @Builder
    public MemberDTO(Long id, String name, Long teamId, Position position, String ranked, Address address) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
        this.position = position;
        this.ranked = ranked;
        this.address = address;
    }
}
