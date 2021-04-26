package com.assign.organization.domain.member;

import com.assign.organization.domain.team.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Member {

    private static final String DEFAULT_DUTY = "'없음'";
    private static final String DEFAULT_POSITION = "'사원'";

    @Id
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Embedded
    private Contact contact;

    @ColumnDefault(value = DEFAULT_POSITION)
    private String position;

    @ColumnDefault(value = DEFAULT_DUTY)
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
        team.getMembers().add(this);
    }

    public void changeRanked(String duty) {
        this.duty = duty;
    }

    public void update(MemberDTO dto) {
        this.name = dto.getName();
        this.position = dto.getPosition();
        this.duty = dto.getDuty();
        this.contact = dto.getContact();
    }

    public void update(Contact contact) {
        this.contact = contact;
    }
}
