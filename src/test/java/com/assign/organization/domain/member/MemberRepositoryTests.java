package com.assign.organization.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testRead() {

        // given
        Member member = Member
                .builder()
                .id(1L)
                .contact(null)
                .name("홍길동")
                .position("인턴")
                .build();

        // when
        memberRepository.save(member);
        memberRepository.flush();

        Optional<Member> findMember = memberRepository.findById(member.getId());

        // then
        assertTrue(findMember.isPresent());
        assertEquals(member.getName(), findMember.get().getName());

    }

    @Test
    public void testInsert() {

        //given
        Contact address = Contact
                .builder()
                .businessCall("1101")
                .cellPhone("010-3358-9731")
                .build();


        Member member = Member
                .builder()
                .id(1L)
                .contact(address)
                .name("홍길동")
                .position("인턴")
                .build();

        // when
        memberRepository.save(member);

        // then
        assertEquals(member.getName(), memberRepository.getOne(member.getId()).getName());
    }

    @Test
    public void testDeleteById() {

        //given
        Contact address = Contact
                .builder()
                .businessCall("1101")
                .cellPhone("010-3358-9731")
                .build();


        Member member = Member
                .builder()
                .id(1L)
                .contact(address)
                .name("홍길동")
                .position("인턴")
                .build();

        // when
        memberRepository.save(member);
        memberRepository.flush();

        Member findMember = memberRepository.getOne(member.getId());

        // then
        assertNotNull(findMember);

        // when
        memberRepository.deleteById(member.getId());
        Optional<Member> deletedMember = memberRepository.findById(member.getId());

        // then
        assertFalse(deletedMember.isPresent());
    }

    @Test
    public void testDeleteByName() {

        //given
        Contact address = Contact
                .builder()
                .businessCall("1101")
                .cellPhone("010-3358-9731")
                .build();


        Member member = Member
                .builder()
                .id(1L)
                .contact(address)
                .name("홍길동")
                .position("인턴")
                .build();

        memberRepository.save(member);

        // when
        int deletedCount = memberRepository.deleteByName(member.getName());

        // then
        assertEquals(1, deletedCount);

        // when
        deletedCount = memberRepository.deleteByName(member.getName());

        // then
        assertEquals(0, deletedCount);

        Optional<Member> findMember = memberRepository.findById(member.getId());

        assertFalse(findMember.isPresent());

    }

    @Test
    public void testFindByLikeAllField() {

        // given
        Set<Member> members = new HashSet<>();

        for(int i = 0; i < 10; i++) {
            Contact address = Contact
                    .builder()
                    .businessCall("100" + i)
                    .cellPhone("010-0000-000" + i)
                    .build();


            Member member = Member
                    .builder()
                    .id((long) i)
                    .contact(address)
                    .name("사원" + i)
                    .position("인턴")
                    .duty("사원")
                    .build();

            members.add(member);
        }

        memberRepository.saveAll(members);

        // when
        String nameKeyword = "";

        List<Member> findMembers = memberRepository.findByLikeAllField(nameKeyword);
        log.info(findMembers.stream().map(Member::getName).collect(Collectors.toList()).toString());

        // then
        assertEquals(10, findMembers.size());

        // when
        String businessCallKeyword = "100";

        findMembers = memberRepository.findByLikeAllField(businessCallKeyword);
        log.info(findMembers.stream().map(m -> m.getContact().getBusinessCall()).collect(Collectors.toList()).toString());

        // then
        assertEquals(10, findMembers.size());

        // when
        String cellPhoneKeyword = "010-0000-000";

        findMembers = memberRepository.findByLikeAllField(cellPhoneKeyword);
        log.info(findMembers.stream().map(m -> m.getContact().getCellPhone()).collect(Collectors.toList()).toString());

        // then
        assertEquals(10, findMembers.size());

    }

}