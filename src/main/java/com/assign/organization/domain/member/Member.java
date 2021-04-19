package com.assign.organization.domain.member;

import com.assign.organization.domain.ranked.Ranked;
import com.assign.organization.domain.team.Team;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Member {

    @Id
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Embedded
    private Address address;

    private Position position;

    @OneToOne
    @JoinColumn(name = "RANKED_ID")
    private Ranked ranked;

    @Builder
    public Member(Long id, String name, Address address, Position position) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.position = position;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
