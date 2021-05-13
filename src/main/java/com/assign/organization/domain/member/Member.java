package com.assign.organization.domain.member;

import com.assign.organization.domain.team.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter()
@Entity
@ToString(exclude = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id_;

    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDate enteredDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Column(nullable = false, unique = true)
    private String businessCall;

    @Column(nullable = false, unique = true)
    private String cellPhone;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String duty;

    @Builder
    public Member(Long id, String name, LocalDate enteredDate, Team team, String businessCall, String cellPhone, String position, String duty) {
        this.id = id;
        this.name = name;
        this.enteredDate = enteredDate;
        this.team = team;
        this.businessCall = businessCall;
        this.cellPhone = cellPhone;
        this.position = position;
        this.duty = duty;
    }

    public void setTeam(Team team) {
        if (this.team != null) {
            this.team.getMembers().removeIf(m -> Objects.equals(m, this));
        }

        this.team = team;
        team.getMembers().add(this);
    }
}
