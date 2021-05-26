package com.assign.organization.domain.member;

import com.assign.organization.exception.NullMemberException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class MemberVOTests {

    @Test
    void testFrom() {
        assertThrows(NullMemberException.class, () -> {
            MemberVO.from(null);
        });
    }

}