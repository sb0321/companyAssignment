package com.assign.organization.domain.team;

import com.assign.organization.utils.CSVMemberVO;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.exception.InvalidCSVFileException;
import com.assign.organization.utils.CSVReader;
import com.assign.organization.utils.NameGenerator;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DataJpaTest
@Transactional
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeamRepositoryTests {

    @TestConfiguration
    static class TeamRepositoryTestsConfiguration {

        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberRepository memberRepository;

    @Value(value = "${csv.data.success}")
    String CSV_FILE_PATH;

    @BeforeAll
    void init() throws InvalidCSVFileException, IOException {

        CSVReader.setCSVFile(CSV_FILE_PATH);
        List<CSVMemberVO> csvMemberVOList = CSVReader.readCSVMemberVOList(1000);
        CSVReader.close();

        Map<String, Team> teams = new HashMap<>();
        Map<String, Integer> memberNameDuplication = new HashMap<>();

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {

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

            member.setTeam(teams.get(csvMemberVO.getTeamName()));
        }
        teamRepository.saveAll(teams.values());
    }

    @ParameterizedTest
    @ValueSource(strings = {"웹개발 1팀", "웹개발 2팀", "모비즌셀", "영업지원부"})
    void testCountTeamNameDuplication(String teamName) {
        long duplicationCount = teamRepository.countTeamNameDuplication(teamName);
        assertEquals(1, duplicationCount);
    }

    @Test
    void testFindAllTeamsOrderByTeamName() {
        List<Team> findTeamList = teamRepository.findAllTeamsOrderByTeamName();

        for (Team team : findTeamList) {
            log.info(team.getName());
            log.info(team.getMembers().stream().map(Member::getName).collect(Collectors.toList()).toString());
        }

        checkTeamNameOrdered(findTeamList);
    }

    void checkTeamNameOrdered(List<Team> teamList) {
        List<String> teamNameList = new ArrayList<>();
        teamList.forEach(t -> teamNameList.add(t.getName()));

        teamNameList.sort(String::compareTo);

        for (int i = 0; i < teamList.size(); i++) {
            assertEquals(teamList.get(i).getName(), teamNameList.get(i));
        }
    }

}
