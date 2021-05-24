package com.assign.organization.domain.member;

import com.assign.organization.domain.team.Team;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class MemberTests {

    @Test
    void testCreateMember() {
        Member member = new Member();
        assertNotNull(member);
    }

    @Test
    void testSetTeam() {
        Member member = new Member();
        Team team = new Team("test");

        member.setTeam(team);
        assertEquals(team, member.getTeam());
    }
}