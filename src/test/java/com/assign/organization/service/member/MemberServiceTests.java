package com.assign.organization.service.member;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.member.Nationality;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:application.properties")
class MemberServiceTests {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @BeforeAll
    void init() {
        Member member = Member
                .builder()
                .id(1L)
                .lastName("lastName")
                .firstName("firstName")
                .businessCall("0000")
                .cellPhone("010-0000-0000")
                .duty("duty")
                .position("position")
                .enteredDate(LocalDate.now())
                .nationality(Nationality.KOREA)
                .build();

        memberRepository.save(member);
    }

    @Test
    void testFindMembersContainsKeyword() {
        assertDoesNotThrow(() -> memberService.findMembersContainsKeyword(null));

        String findKeyword = "firstName";
        assertDoesNotThrow(() -> {
            List<MemberVO> findMemberVOList = memberService.findMembersContainsKeyword(findKeyword);
            assertFalse(findMemberVOList.isEmpty());
        });

        String notFindKeyword = "nothing";
        assertDoesNotThrow(() -> {
            List<MemberVO> emptyList = memberService.findMembersContainsKeyword(notFindKeyword);
            assertTrue(emptyList.isEmpty());
        });
    }


    @Test
    void testCountFirstNameContains() {
        assertThrows(NullMemberNameException.class, () -> memberService.countFirstNameContains(null));

        String firstName = "firstName";
        assertDoesNotThrow(() -> {
            long count = memberService.countFirstNameContains(firstName);
            assertEquals(1L, count);
        });

        String noDuplicatedName = "nothing";
        assertDoesNotThrow(() -> {});
        long count = memberService.countFirstNameContains(noDuplicatedName);
        assertEquals(0L, count);
    }

    @Test
    void testCheckMemberIdDuplication() {
        assertThrows(NullMemberIdException.class, () -> memberService.checkMemberIdDuplication(null));

        Long duplicatedMemberId = 1L;
        assertThrows(MemberIdDuplicationException.class, () -> memberService.checkMemberIdDuplication(duplicatedMemberId));

        Long noDuplicatedMemberId = 2L;
        assertDoesNotThrow(() -> memberService.checkMemberIdDuplication(noDuplicatedMemberId));
    }
}