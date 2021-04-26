package com.assign.organization.domain.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberDTO;
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

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MEMBER_ID")
    private Member teamLeader;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    public MemberDTO getTeamLeaderDTO() {

        return MemberDTO
                .builder()
                .name(teamLeader.getName())
                .contact(teamLeader.getContact())
                .teamId(teamLeader.getId())
                .duty(teamLeader.getDuty())
                .build();
    }

    public void addMember(Member member) {
        this.members.add(member);
        member.changeTeam(this);
    }

    public void removeMember(Member member) {
        this.members.removeIf(m -> Objects.equals(m, member));
        member.changeTeam(null);
    }

    public Set<MemberDTO> getTeamMembersDTO() {

        Set<MemberDTO> teamMembers = new HashSet<>();

        for (Member member : this.members) {
            MemberDTO dto = MemberDTO
                    .builder()
                    .name(teamLeader.getName())
                    .contact(teamLeader.getContact())
                    .teamId(teamLeader.getId())
                    .duty(teamLeader.getDuty())
                    .build();

            teamMembers.add(dto);
        }

        return teamMembers;
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
