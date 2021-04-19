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
class MemberRepositoryTest {

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

        String afterName = "홍길이";
        Position afterPosition = Position.CHAIRMAN;

        member.update(afterName, afterPosition);

        Member findMember = memberRepository.getOne(member.getId());

        // then
        assertEquals(afterName, findMember.getName());
        assertEquals(afterPosition, findMember.getPosition());

    }

    @Test
    public void testAddressUpdate() {

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

        Address beforeAddress = member.getAddress();

        Address newAddress = Address
                .builder()
                .cellPhone("010-3333-3333")
                .businessCall("1000")
                .build();

        member.updateAddress(newAddress);

        memberRepository.flush();

        Member findMember = memberRepository.getOne(member.getId());

        // then
        assertNotEquals(beforeAddress, findMember.getAddress());

        log.info(beforeAddress.getBusinessCall() + " || " + findMember.getAddress().getBusinessCall());

        assertNotEquals(beforeAddress.getBusinessCall(), findMember.getAddress().getBusinessCall());
        assertNotEquals(beforeAddress.getCellPhone(), findMember.getAddress().getCellPhone());

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