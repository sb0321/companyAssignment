package com.assign.organization.domain.member;

import com.assign.organization.domain.team.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
public class Member {

    private static final String LEADER = "팀장";

    @Id
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Embedded
    private Contact contact;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String duty;

    @Builder
    public Member(Long id, String name, Contact contact, String position, String duty) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.position = position;
        this.duty = duty;
    }

    public void changeTeam(Team team) {
        this.team = team;
    }

    public boolean isLeader() {
        return this.duty.equals(LEADER);
    }
}
