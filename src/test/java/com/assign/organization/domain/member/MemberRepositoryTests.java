package com.assign.organization.domain.member;

import com.assign.organization.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTests {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void tearDown() {
        memberRepository.deleteAll();
    }


    @Test
    public void testCountNameContains() {

        // given
        List<Member> memberList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Member member = Member
                    .builder()
                    .name("test" + i)
                    .position("사원")
                    .duty("팀장")
                    .build();

            memberList.add(member);
        }

        memberRepository.saveAll(memberList);

        String nameContains = "test";

        // when
        long countNameContains = memberRepository.countNameContains(nameContains);

        // then
        assertEquals(memberList.size(), countNameContains);

    }

    @Test
    public void testFindMembersContainsKeyword() {

        // given
        List<Member> memberList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            Contact contact = Contact
                    .builder()
                    .businessCall("100" + i)
                    .cellPhone("010-0000-000" + i)
                    .build();

            Member member = Member
                    .builder()
                    .name("test" + i)
                    .position("사원")
                    .duty("팀장")
                    .contact(contact)
                    .build();

            memberList.add(member);
        }

        memberRepository.saveAll(memberList);

        // when
        String nameKeyword = "test";
        List<Member> findNameMemberList = memberRepository.findMembersContainsKeyword(nameKeyword);

        log.info(findNameMemberList.toString());

        // then
        assertEquals(memberList.size(), findNameMemberList.size());

        // when
        String cellPhoneKeyword = "010-0000-000";
        List<Member> findCellPhoneMemberList = memberRepository.findMembersContainsKeyword(cellPhoneKeyword);

        log.info(findCellPhoneMemberList.toString());

        // then
        assertEquals(memberList.size(), findCellPhoneMemberList.size());

        // when
        String businessCallKeyword = "100";
        List<Member> findBusinessCallMemberList = memberRepository.findMembersContainsKeyword(businessCallKeyword);

        log.info(findBusinessCallMemberList.toString());

        // then
        assertEquals(memberList.size(), findBusinessCallMemberList.size());


        // when
        String teamNameKeyword = "team";
        List<Member> findTeamNameMemberList = memberRepository.findMembersContainsKeyword(teamNameKeyword);

        log.info(findTeamNameMemberList.toString());

        // then
        assertEquals(0, findTeamNameMemberList.size());
    }

}