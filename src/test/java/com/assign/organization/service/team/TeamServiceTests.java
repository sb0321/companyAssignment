package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.Nationality;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.exception.NullCSVFilePathException;
import com.assign.organization.utils.CSVReader;
import com.assign.organization.utils.DuplicateNameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
class TeamServiceTests {

    @Autowired
    TeamService teamService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void purge() {
        memberRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    void testFindAllTeamVOList() {
        givenTeamsAndMembers();
        List<TeamVO> teamList = whenFindAllTeamVOList();
        thenFindAllTeamVOListReturnsList(teamList);
    }

    void givenTeamsAndMembers() {
        Team team = new Team("test");
        List<Member> memberList = makeMembers();

        for (Member member : memberList) {
            member.setTeam(team);
        }

        teamRepository.save(team);
    }

    List<Member> makeMembers() {
        List<Member> memberList = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Member member = Member
                    .builder()
                    .id((long) i)
                    .firstName("firstName" + i)
                    .lastName("lastName" + i)
                    .nationality(Nationality.KOREA)
                    .enteredDate(LocalDate.now())
                    .position("position")
                    .businessCall("000" + i)
                    .cellPhone("010-0000-000" + i)
                    .duty("duty")
                    .build();

            memberList.add(member);
        }

        return memberList;
    }

    List<TeamVO> whenFindAllTeamVOList() {
        return teamService.findAllTeamVOList();
    }

    void thenFindAllTeamVOListReturnsList(List<TeamVO> teamList) {
        assertFalse(teamList.isEmpty());
    }

    @Test
    void testInsertTeamsFromDataPath() {
        assertThrows(NullCSVFilePathException.class, () -> teamService.insertTeamsFromDataPath(null));
        assertDoesNotThrow(() -> teamService
                .insertTeamsFromDataPath(new File("src/test/resources/data/data.csv").getAbsolutePath()));
    }

}