package com.assign.organization.service.member;

import com.assign.organization.domain.member.*;
import com.assign.organization.domain.ranked.Ranked;
import com.assign.organization.domain.team.Team;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Long MEMBER_ID;
    private String MEMBER_NAME;

    @BeforeEach
    public void init() {
        Address address = Address
                .builder()
                .cellPhone("010-1111-1111")
                .businessCall("1101")
                .build();

        Member member = Member
                .builder()
                .name("test")
                .position(Position.INTERN)
                .address(address)
                .build();

        Team team = Team
                .builder()
                .name("testTeam")
                .build();

        Ranked ranked = Ranked
                .builder()
                .name("superRank")
                .build();

        member.changeTeam(team);
        member.changeRanked(ranked);

        MEMBER_ID = member.getId();
        MEMBER_NAME = member.getName();

        Mockito.when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        Mockito.when(memberRepository.deleteByName(member.getName())).thenReturn(1);

    }

    @Test
    public void testFindMemberById() {

        MemberDTO findMemberDTO = memberService.findMemberById(MEMBER_ID);

        log.debug(findMemberDTO.toString());

        assertNotNull(findMemberDTO);
    }

    @Test
    public void testDeleteMemberByName() {

        assertTrue(memberService.deleteMemberByName(MEMBER_NAME));
    }

    @Test
    public void testUpdateMemberAddress() {

        // given
        Address address = Address
                .builder()
                .cellPhone("010-3454-2143")
                .businessCall("1111")
                .build();

        // when
        MemberDTO memberDTO = memberService.updateMemberAddress(MEMBER_ID, address);

        // then
        assertEquals(address.getBusinessCall(), memberDTO.getAddress().getBusinessCall());
        assertEquals(address.getCellPhone(), memberDTO.getAddress().getCellPhone());

    }

    @Test
    public void testUpdateMember() {

        // given
        String newName = "newName";
        Position newPosition = Position.CHAIRMAN;

        // when
        MemberDTO memberDTO = memberService.updateMember(MEMBER_ID, newPosition, newName);

        // then
        assertEquals(newName, memberDTO.getName());
        assertEquals(newPosition, memberDTO.getPosition());

    }


}