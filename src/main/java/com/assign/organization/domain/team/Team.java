package com.assign.organization.domain.team;

import com.assign.organization.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEAM_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private final Set<Member> members = new HashSet<>();

    @Builder
    public Team(String name) {
        this.name = name;
    }

    public void addTeamMember(Member member) {
        this.members.add(member);
        member.changeTeam(this);
    }
}
