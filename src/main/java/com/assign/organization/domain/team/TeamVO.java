package com.assign.organization.domain.team;

import com.assign.organization.domain.member.MemberVO;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TeamVO {

    private Long id;
    private String name;
    private MemberVO teamLeader;
    private List<MemberVO> members;

    @Builder
    public TeamVO(Long id, String name, MemberVO teamLeader, List<MemberVO> members) {
        this.id = id;
        this.name = name;
        this.teamLeader = teamLeader;
        this.members = members;
    }
}
