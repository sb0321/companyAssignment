package com.assign.organization.domain.team;


import com.assign.organization.domain.member.MemberDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@ToString
public class TeamDTO {

    private Long id;
    private String name;

    private MemberDTO teamLeader;

    private Set<MemberDTO> members;

    @Builder
    public TeamDTO(Long id, String name, MemberDTO teamLeader, Set<MemberDTO> members) {
        this.id = id;
        this.name = name;
        this.teamLeader = teamLeader;
        this.members = members;
    }
}
