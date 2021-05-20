package com.assign.organization.domain.team;

import com.assign.organization.domain.member.Member;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamTests {

    @Test
    void testAddMember() {
        Team team = new Team("test");
        Member member = Member
                .builder()
                .id(1L)
                .build();

        team.addMember(member);

        assertEquals(member, team.getMembers().get(0));
    }
}