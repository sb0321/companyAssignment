package com.assign.organization.domain.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.Nationality;
import com.assign.organization.exception.NullTeamException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class TeamVOTests {

    @Test
    void testFrom() {
        Team nullTeam = null;
        Exception exception = whenFromThrowsNullTeamException(nullTeam);
        thenThrowsExceptionIsNullTeamException(exception);

        Team team = new Team("team");
        List<Member> memberList = makeMembers();
        makeConnectionBetweenTeamAndMembers(team, memberList);
        thenFromSuccess(team, memberList);
    }

    Exception whenFromThrowsNullTeamException(Team team) {
        return assertThrows(NullTeamException.class, () -> TeamVO.from(team));
    }

    void thenThrowsExceptionIsNullTeamException(Exception e) {
        assertEquals(NullTeamException.class, e.getClass());
    }

    List<Member> makeMembers() {
        List<Member> memberList = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Member member = Member
                    .builder()
                    .id((long)i)
                    .firstName("firstName" + i)
                    .lastName("lastName" + i)
                    .nationality(Nationality.KOREA)
                    .enteredDate(LocalDate.now())
                    .position("position")
                    .businessCall("000" + i)
                    .cellPhone("010-0000-000" + i)
                    .duty("duty")
                    .build();

            memberList.add(member);
        }
        return memberList;
    }

    void makeConnectionBetweenTeamAndMembers(Team team, List<Member> memberList) {
        for (Member member : memberList) {
            member.setTeam(team);
        }
    }

    void thenFromSuccess(Team team, List<Member> memberList) {
        TeamVO teamVO = TeamVO.from(team);

        assertEquals(team.getId(), teamVO.getId());
        assertEquals(team.getName(), teamVO.getName());
        assertEquals(memberList.size(), teamVO.getMembers().size());
    }

}