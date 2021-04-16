package com.assign.organization.domain.member;

import com.assign.organization.domain.rank.Rank;
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
    @JoinColumn(name = "RANK_ID")
    private Rank rank;

    @Builder
    public Member(Long id, String name, Address address, Position position, Rank rank) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.position = position;
        this.rank = rank;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
