package com.assign.organization.domain.member;

import com.assign.organization.domain.team.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Embedded
    private Contact contact;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String duty;

    @Builder
    public Member(String name, Contact contact, String position, String duty) {
        this.name = name;
        this.contact = contact;
        this.position = position;
        this.duty = duty;
    }

    public void changeTeam(Team team) {

        if(this.team != null) {
            this.team.getMembers().removeIf(m -> Objects.equals(m, this));
        }

        this.team = team;
        team.getMembers().add(this);
    }
}
