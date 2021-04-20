package com.assign.organization.domain.member;

import com.assign.organization.domain.team.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Position position;

    private String ranked;

    @Builder
    public Member(String name, Address address, Position position, String ranked) {
        this.name = name;
        this.address = address;
        this.position = position;
        this.ranked = ranked;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    public void changeRanked(String ranked) {
        this.ranked = ranked;
    }

    public void updateAddress(Address address) {
        this.address = address;
    }

    public void update(String name, Position position) {
        this.name = name;
        this.position = position;
    }
}
