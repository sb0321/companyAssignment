package com.assign.organization.service.member;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Contact;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberServiceTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberService memberService;

    @Test
    @Transactional
    public void testIsertMembersFromCSVMemberVOList() {

        // given
        List<CSVMemberVO> csvMemberVOList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CSVMemberVO csvMemberVO = CSVMemberVO
                    .builder()
                    .id((long) i)
                    .name("test" + i)
                    .teamName("team" + i)
                    .businessCall("100" + i)
                    .cellPhone("010-0000-000" + i)
                    .position("사원")
                    .duty("팀장")
                    .build();

            csvMemberVOList.add(csvMemberVO);
        }

        // when
        memberService.insertMembersFromCSVMemberVOList(csvMemberVOList);

        // then
        List<Member> findMemberList = (List<Member>) memberRepository.findAll();

        assertEquals(csvMemberVOList.size(), findMemberList.size());

        log.info(findMemberList.toString());

        for (int i = 0; i < findMemberList.size(); i++) {
            assertEquals(csvMemberVOList.get(i).getName(), findMemberList.get(i).getName());
        }

    }

    @Test
    @Transactional
    public void testFindMembersContainsKeyword() {

        // given
        Contact contact = Contact
                .builder()
                .businessCall("1001")
                .cellPhone("010-1234-1111")
                .build();

        Member member = Member
                .builder()
                .id(1L)
                .name("test")
                .contact(contact)
                .duty("팀장")
                .position("차장")
                .build();

        Team team = Team
                .builder()
                .name("testTeam")
                .build();

        teamRepository.save(team);

        member.changeTeam(team);

        memberRepository.save(member);

        // when
        String nameKeyword = "test";

        List<MemberVO> findMembers = memberService.findMembersContainsKeyword(nameKeyword);
        log.info(findMembers.toString());
        assertEquals(member.getName(), findMembers.get(0).getName());

        // when
        String businessCallKeyword = "1001";

        findMembers = memberService.findMembersContainsKeyword(businessCallKeyword);
        log.info(findMembers.toString());
        assertEquals(member.getContact().getBusinessCall(), findMembers.get(0).getBusinessCall());

        // when
        String cellPhoneKeyword = "010-1234-1111";

        findMembers = memberService.findMembersContainsKeyword(cellPhoneKeyword);
        log.info(findMembers.toString());
        assertEquals(member.getContact().getCellPhone(), findMembers.get(0).getCellPhone());

        // when
        String teamNameKeyword = "testTeam";

        findMembers = memberService.findMembersContainsKeyword(teamNameKeyword);
        log.info(findMembers.toString());
        assertEquals(member.getTeam().getName(), findMembers.get(0).getTeamName());

    }

    @Test
    public void testFindMemberById() {

        final Long memberId = 1L;

        // given
        Member member = Member
                .builder()
                .id(memberId)
                .name("test")
                .duty("팀장")
                .position("차장")
                .build();

        memberRepository.save(member);

        // ok
        memberService.findMemberById(memberId);

        // fail
        try {
            memberService.findMemberById(2L);
            fail("없는 아이디로 검색했을 때 예외가 발생해야 합니다.");
        } catch (Exception e) {
            // PASS
        }

    }
}