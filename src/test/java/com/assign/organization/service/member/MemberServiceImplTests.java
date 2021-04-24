package com.assign.organization.service.member;

import com.assign.organization.domain.member.*;
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
import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class MemberServiceImplTests {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Long MEMBER_ID;
    private String MEMBER_NAME;

    private Member MEMBER;

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
                .position("인턴")
                .address(address)
                .build();

        MEMBER = member;

        Team team = Team
                .builder()
                .name("testTeam")
                .build();


        member.changeTeam(team);
        member.changeRanked("팀장");

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
    public void testUpdateMember() {

        // given
        String newName = "newName";
        String newString = "사장";

        // when
//        MemberDTO memberDTO = memberService.updateMember(MEMBER_ID, newPosition, newName);

        // then
//        assertEquals(newName, memberDTO.getName());
//        assertEquals(newPosition, memberDTO.getPosition());

    }

    @Test
    public void testFindMemberByIdEntity() {

        // when
        Optional<Member> findMember = memberService.findMemberByIdEntity(MEMBER_ID);

        // then
        assertTrue(findMember.isPresent());

        assertEquals(MEMBER, findMember.get());

    }

    @Test
    public void testCreateMember() {

        // given
        MemberVO vo = MemberVO
                .builder()
                .name("newMember")
                .duty("팀장")
                .businessCall("1011")
                .cellPhone("010-1111-1111")
                .build();

        Address address = Address
                .builder()
                .businessCall("1011")
                .cellPhone("010-1111-1111")
                .build();

        Member member = Member
                .builder()
                .name(vo.getName())
                .duty(vo.getDuty())
                .address(address)
                .build();

        Mockito.when(memberRepository.save(any())).thenReturn(member);

        // when
        MemberDTO dto = memberService.createMember(vo);

        // then
        assertEquals(vo.getName(), dto.getName());
        assertEquals(vo.getBusinessCall(), dto.getAddress().getBusinessCall());
        assertEquals(vo.getCellPhone(), dto.getAddress().getCellPhone());
        assertEquals(vo.getDuty(), dto.getDuty());

    }


}