package com.assign.organization.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class MemberTests {

    @Test
    void testCreateMember() {
        Member member = new Member();
        assertNotNull(member);
    }
}