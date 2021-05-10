package com.assign.organization.domain.team;

import com.assign.organization.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEAM_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private final List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
