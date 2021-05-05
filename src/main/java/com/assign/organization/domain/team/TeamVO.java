package com.assign.organization.domain.team;

import com.assign.organization.domain.member.MemberVO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class TeamVO {

    private final Long id;
    private final String name;
    private final List<MemberVO> members;

    @Builder
    public TeamVO(Long id, String name, List<MemberVO> members) {
        this.id = id;
        this.name = name;
        this.members = members;
    }
}
