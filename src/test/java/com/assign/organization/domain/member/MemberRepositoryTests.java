package com.assign.organization.domain.member;

import com.assign.organization.config.JpaDatabaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Import(JpaDatabaseConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testRead() {

        // given
        Member member = Member
                .builder()
                .address(null)
                .name("홍길동")
                .position(Position.INTERN)
                .build();

        // when
        memberRepository.save(member);
        memberRepository.flush();

        Optional<Member> findMember = memberRepository.findById(member.getId());

        // then
        assertFalse(findMember.isEmpty());
        assertEquals(member, findMember.get());

    }

    @Test
    public void testInsert() {

        //given
        Address address = Address
                .builder()
                .businessCall("1101")
                .cellPhone("010-3358-9731")
                .build();


        Member member = Member
                .builder()
                .address(address)
                .name("홍길동")
                .position(Position.INTERN)
                .build();

        // when
        memberRepository.save(member);

        // then
        assertEquals(member, memberRepository.getOne(member.getId()));
    }

    @Test
    public void testUpdate() {

        // given
        Address address = Address
                .builder()
                .businessCall("1101")
                .cellPhone("010-3358-9731")
                .build();

        String beforeName = "홍길동";
        Position beforePosition = Position.INTERN;

        Member member = Member
                .builder()
                .address(address)
                .name(beforeName)
                .position(beforePosition)
                .build();

        memberRepository.save(member);
        memberRepository.flush();

        // when

        String afterName = "홍길이";
        Position newPosition = Position.CHAIRMAN;

        Address newAddress = Address
                .builder()
                .businessCall("0000")
                .cellPhone("010-2222-2222")
                .build();

        MemberDTO dto = MemberDTO
                .builder()
                .address(newAddress)
                .position(newPosition)
                .ranked("사원")
                .name(afterName)
                .build();

        Member findMember = memberRepository.getOne(member.getId());
        findMember.update(dto);

        // then
        assertEquals(afterName, findMember.getName());
        assertEquals(newPosition, findMember.getPosition());
        assertEquals(newAddress, findMember.getAddress());

    }

    @Test
    public void testDeleteById() {

        //given
        Address address = Address
                .builder()
                .businessCall("1101")
                .cellPhone("010-3358-9731")
                .build();


        Member member = Member
                .builder()
                .address(address)
                .name("홍길동")
                .position(Position.INTERN)
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
        assertTrue(deletedMember.isEmpty());
    }

    @Test
    public void testDeleteByName() {

        //given
        Address address = Address
                .builder()
                .businessCall("1101")
                .cellPhone("010-3358-9731")
                .build();


        Member member = Member
                .builder()
                .address(address)
                .name("홍길동")
                .position(Position.INTERN)
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

        assertTrue(findMember.isEmpty());

    }

}