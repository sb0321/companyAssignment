package com.assign.organization.domain.member;

import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.exception.InvalidCSVFileException;
import com.assign.organization.utils.CSVMemberVO;
import com.assign.organization.utils.CSVReader;
import com.assign.organization.utils.NameGenerator;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
@Transactional
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTests {

    @TestConfiguration
    static class MemberRepositoryTestsConfiguration {

        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Value("${csv.data.success}")
    String CSV_FILE_PATH;


    @BeforeAll
    void init() throws InvalidCSVFileException, IOException {
        Map<String, Team> teams = new HashMap<>();
        Map<String, Integer> memberNameDuplication = new HashMap<>();

        CSVReader.setCSVFile(CSV_FILE_PATH);
        for (CSVMemberVO csvMemberVO : getCSVMemberVOList()) {

            log.info(csvMemberVO.toString());

            memberNameDuplication.putIfAbsent(csvMemberVO.getLastName(), -1);
            memberNameDuplication.replace(csvMemberVO.getLastName(),
                    memberNameDuplication.get(csvMemberVO.getLastName()) + 1);


            teams.putIfAbsent(csvMemberVO.getTeamName(), new Team(csvMemberVO.getTeamName()));

            String newLastName = NameGenerator
                    .generateNameWhenDuplication(csvMemberVO.getLastName(), memberNameDuplication.get(csvMemberVO.getLastName()));

            Member member = Member
                    .builder()
                    .id(csvMemberVO.getMemberId())
                    .lastName(newLastName)
                    .firstName(csvMemberVO.getFirstName())
                    .enteredDate(csvMemberVO.getEnteredDate())
                    .position(csvMemberVO.getPosition())
                    .duty(csvMemberVO.getDuty())
                    .businessCall(csvMemberVO.getBusinessCall())
                    .cellPhone(csvMemberVO.getCellPhone())
                    .build();

            teams.get(csvMemberVO.getTeamName()).addMember(member);
        }
        CSVReader.close();
        teamRepository.saveAll(teams.values());
    }

    List<CSVMemberVO> getCSVMemberVOList() throws InvalidCSVFileException, IOException {
        List<CSVMemberVO> list = new LinkedList<>();
        CSVReader.setCSVFile(CSV_FILE_PATH);
        while (true) {
            List<CSVMemberVO> load = CSVReader.readCSVMemberVOList(100);
            if (load.isEmpty()) {
                break;
            }
            list.addAll(load);
        }
        CSVReader.close();
        return list;
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
                member.getCellPhone().contains(keyword) ||
                member.getBusinessCall().contains(keyword);
    }
}