package com.assign.organization.domain.team;

import com.assign.organization.domain.member.MemberVO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TeamVO {

    private Long id;
    private String name;
    private List<MemberVO> members;
}
