package com.assign.organization.domain.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.exception.NullTeamException;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class TeamVO {

    private Long id;
    private String name;
    private List<MemberVO> members;

    public static TeamVO from(Team team) {
        if (team == null) {
            throw new NullTeamException();
        }

        List<MemberVO> memberVOList = convertMemberListToMemberVOList(team.getMembers());
        return new TeamVO(team.getId(), team.getName(), memberVOList);
    }

    private static List<MemberVO> convertMemberListToMemberVOList(List<Member> memberList) {
        return memberList.stream().map(MemberVO::from).collect(Collectors.toList());
    }
}
