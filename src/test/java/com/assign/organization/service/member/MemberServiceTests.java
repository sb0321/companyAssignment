package com.assign.organization.service.member;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberRepository;
import com.assign.organization.domain.member.MemberVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberServiceTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    public void testFindMemberById() {

        final Long memberId = 1L;

        // given
        Member member = Member
                .builder()
                .id(memberId)
                .name("test")
                .duty("팀장")
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

    @Test
    void testCreateMemberFromMemberVO() {

        // given
        MemberVO newMemberVO = MemberVO
                .builder()
                .id(1L)
                .name("test")
                .businessCall("1001")
                .cellPhone("010-1111-1111")
               .duty("팀장")
               .position("사원")
                .build();

        // ok
        memberService.createMemberFromMemberVO(newMemberVO);
    }
}