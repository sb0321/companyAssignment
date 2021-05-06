package com.assign.organization.domain.member;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Transactional
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTests {

    @Autowired
    MemberRepository memberRepository;

    @Value("${csv.data.success}")
    String CSV_FILE_PATH;

    @BeforeAll
    void init() throws CSVFileInvalidException {
        List<CSVMemberVO> csvMemberVOList = CSVReader.readCSVFile(CSV_FILE_PATH);

        Set<Member> members = new HashSet<>();
        Map<String, Team> teams = new HashMap<>();
        Map<String, Integer> memberNameDuplication = new HashMap<>();

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {

            log.info(csvMemberVO.toString());

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
    @ValueSource(strings = {"사원", "1000", "010-0000-0001", "웹개발 1팀"})
    void testFindMembersContainsKeyword(String keyword) {
        List<Member> findMembers = memberRepository.findMembersContainsKeyword(keyword);
        log.info(findMembers.toString());

        for (Member findMember : findMembers) {
            assertTrue(checkKeywordContains(findMember, keyword));
        }
    }

    boolean checkKeywordContains(Member member, String keyword) {
        return member.getName().contains(keyword) ||
                member.getTeam().getName().contains(keyword) ||
                member.getContact().getCellPhone().contains(keyword) ||
                member.getContact().getBusinessCall().contains(keyword);
    }

    @ParameterizedTest
    @ValueSource(strings = {"사원", "김승빈"})
    void testCountNameContains(String memberName) {
        long count = memberRepository.countNameContains(memberName);

        if (memberName.equals("사원")) {
            assertEquals(9, count);
        }
        if (memberName.equals("김승빈")) {
            assertEquals(3, count);
        }
    }

}