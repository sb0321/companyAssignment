package com.assign.organization.domain.team;

import com.assign.organization.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEAM_ID")
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member teamLeader;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private final Set<Member> members = new HashSet<>();

    @Builder
    public Team(String name) {
        this.name = name;
    }

    public void updateTeamName(String name) {
        this.name = name;
    }

    public void changeTeamLeader(Member member) {
        this.teamLeader = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(getId(), team.getId()) && Objects.equals(getName(), team.getName()) && Objects.equals(getMembers(), team.getMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getMembers());
    }
}
