package com.assign.organization.service.member;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Contact;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.Team;
import com.assign.organization.exception.CSVFileInvalidException;
import com.assign.organization.utils.CSVReader;
import com.assign.organization.utils.NameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberServiceTests {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @BeforeAll
    void init() throws CSVFileInvalidException {
        List<CSVMemberVO> csvMemberVOList = CSVReader.readCSVFile("/Users/sbkim/Downloads/data.csv");

        Set<Member> members = new HashSet<>();
        Map<String, Team> teams = new HashMap<>();
        Map<String, Integer> memberNameDuplication = new HashMap<>();

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {

            memberNameDuplication.putIfAbsent(csvMemberVO.getName(), -1);
            memberNameDuplication.replace(csvMemberVO.getName(), memberNameDuplication.get(csvMemberVO.getName()) + 1);

            teams.putIfAbsent(csvMemberVO.getTeamName(), new Team(csvMemberVO.getTeamName()));

            String newName = NameGenerator.generateNameWhenDuplication(csvMemberVO.getName(), memberNameDuplication.get(csvMemberVO.getName()));
            Contact contact = new Contact(csvMemberVO.getCellPhone(), csvMemberVO.getBusinessCall());
            Member member = Member
                    .builder()
                    .name(newName)
                    .position(csvMemberVO.getPosition())
                    .duty(csvMemberVO.getDuty())
                    .contact(contact)
                    .build();

            member.changeTeam(teams.get(csvMemberVO.getTeamName()));
            members.add(member);
        }

        memberRepository.saveAll(members);
    }


    @ParameterizedTest
    @ValueSource(strings = {"사원", "1000", "010-0000-000", "웹개발 1팀"})
    void testFindMembersContainsKeyword(String keyword) {

        List<MemberVO> findMemberVOList = memberService.findMembersContainsKeyword(keyword);
        log.info(findMemberVOList.toString());

    }



}