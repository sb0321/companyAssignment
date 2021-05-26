package com.assign.organization.domain.member;

import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.exception.NullBusinessCallException;
import com.assign.organization.exception.NullMemberIdException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Transactional
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:application.properties")
class MemberRepositoryTests {

    @TestConfiguration
    static class MemberRepositoryTestsConfiguration {

        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }

    @Autowired
    MemberRepository memberRepository;

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
                .position("postion")
                .enteredDate(LocalDate.now())
                .nationality(Nationality.KOREA)
                .build();

        memberRepository.save(member);
    }

    @Test
    void testCountFirstNameContainsSearchName() {
        String firstName = "firstName";
        long count = whenCountFirstNameContainsSearchName(firstName);
        thenCountFirstNameCountNotZero(count);
    }

    long whenCountFirstNameContainsSearchName(String firstName) {
        return memberRepository.countFirstNameContains(firstName);
    }

    void thenCountFirstNameCountNotZero(long count) {
        assertNotEquals(0L, count);
    }

    @Test
    void testFindMembersContainsKeyword() {
        String nullKeyword = null;
        thenFindMembersContainsNullKeywordThrowsException(nullKeyword);

        String firstNameKeyword = "firstName";
        thenFindMembersContainsKeyword(firstNameKeyword);

        String lastNameKeyword = "lastName";
        thenFindMembersContainsKeyword(lastNameKeyword);

        String businessCallKeyword = "0000";
        thenFindMembersContainsKeyword(businessCallKeyword);

        String cellPhoneKeyword = "010-0000-0000";
        thenFindMembersContainsKeyword(cellPhoneKeyword);

        String nothingKeyword = "nothing";
        thenFindMembersContainsKeywordReturnsEmptyList(nothingKeyword);
    }

    void thenFindMembersContainsNullKeywordThrowsException(String keyword) {
        assertDoesNotThrow(() -> memberRepository.findMembersContainsKeyword(keyword));
    }

    void thenFindMembersContainsKeyword(String keyword) {
        List<Member> findMembers = memberRepository.findMembersContainsKeyword(keyword);
        assertFalse(findMembers.isEmpty());
    }

    void thenFindMembersContainsKeywordReturnsEmptyList(String keyword) {
        List<Member> emptyList = memberRepository.findMembersContainsKeyword(keyword);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    void testCheckMemberIdDuplication() {
        Long nullMemberId = null;
        thenCheckMemberIdDuplicationNullMemberIdThrowsException(nullMemberId);

        Long duplicatedMemberId = 1L;
        thenCheckMemberIdDuplicationReturnsTrue(duplicatedMemberId);

        Long memberId = Long.MAX_VALUE;
        thenCheckMemberIdDuplicationReturnsFalse(memberId);
    }

    void thenCheckMemberIdDuplicationNullMemberIdThrowsException(Long memberId) {
        assertThrows(NullMemberIdException.class, () -> memberRepository.checkMemberIdDuplication(memberId));
    }

    void thenCheckMemberIdDuplicationReturnsTrue(Long memeberId) {
        boolean duplication = memberRepository.checkMemberIdDuplication(memeberId);
        assertTrue(duplication);
    }

    void thenCheckMemberIdDuplicationReturnsFalse(Long memberId) {
        boolean duplication = memberRepository.checkMemberIdDuplication(memberId);
        assertFalse(duplication);
    }

    @Test
    void testCheckBusinessCallDuplication() {
        assertThrows(NullBusinessCallException.class, () -> memberRepository.checkBusinessCallDuplication(null));

        String businessCall = "0000";
        thenBusinessCallDuplicated(businessCall);

        String newBusinessCall = "1111";
        thenBusinessCallNotDuplicated(newBusinessCall);
    }

    void thenBusinessCallDuplicated(String businessCall) {
        assertTrue(memberRepository.checkBusinessCallDuplication(businessCall));
        memberRepository.deleteAll();
    }

    void thenBusinessCallNotDuplicated(String businessCall) {
        assertFalse(memberRepository.checkBusinessCallDuplication(businessCall));
        memberRepository.deleteAll();
    }

}