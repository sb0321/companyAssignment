package com.assign.organization.service.member;

import com.assign.organization.domain.member.Contact;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.Team;
import com.assign.organization.exception.CSVFileInvalidException;
import com.assign.organization.service.team.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:application.properties")
class MemberServiceTests {

    @Value(value = "${csv.data.success}")
    String CSV_FILE_PATH;

    @Mock
    MemberRepository memberRepository;

    @Mock
    TeamService teamService;

    @InjectMocks
    MemberService memberService;


    @Test
    void testFindMembersContainsKeyword() {

        String keyword = "test";

        Contact contact = new Contact("", "");

        Member member = Member
                .builder()
                .name(keyword)
                .duty("")
                .position("")
                .contact(contact)
                .build();

        member.setTeam(new Team(""));

        List<Member> result = new ArrayList<>();
        result.add(member);

        when(memberRepository.findMembersContainsKeyword(any())).thenReturn(result);

        List<MemberVO> findMembers = memberService.findMembersContainsKeyword(keyword);

        assertEquals(keyword, findMembers.stream().findAny().get().getName());
    }

    @Test
    void testInsertMembersFromCSVFile() throws CSVFileInvalidException {
        log.info(CSV_FILE_PATH);
        memberService.insertMembersFromCSVFile(CSV_FILE_PATH);
    }
}