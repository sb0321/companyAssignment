package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberRepository;
import com.assign.organization.domain.team.QTeam;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamRepository;
import com.assign.organization.domain.team.TeamVO;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeamServiceTests {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @AfterEach
    public void tearDown() {
        teamRepository.deleteAll();
    }

    @Test
    public void testAddTeamMember() {

        // given
        Team team = Team
                .builder()
                .name("testTeam")
                .build();

        Team savedTeam = teamRepository.save(team);

        Member member = Member
                .builder()
                .id(1L)
                .name("test")
                .position("사원")
                .duty("팀원")
                .build();

        // when
        teamService.addMemberToTeam(savedTeam, member);
    }

    @Test
    public void testCheckExistWithTeamName() {

        final String teamName = "testTeam";

        // given
        Team team = Team
                .builder()
                .name(teamName)
                .build();

        // when
        teamRepository.save(team);

        // then
        boolean exist = teamService.checkExistWithTeamName(teamName);
        assertTrue(exist);

        exist = teamService.checkExistWithTeamName("nothing");
        assertFalse(exist);
    }

    @Test
    @Transactional
    public void testFindTeamByTeamName() {

        final String testTeamName = "test";

        // given
        Team team = Team
                .builder()
                .name(testTeamName)
                .build();

        teamRepository.save(team);

        // ok
        Team findTeam = teamService.findTeamByTeamName(testTeamName);
        assertEquals(testTeamName, findTeam.getName());

        // fail
        try {
            teamService.findTeamByTeamName("null");
            fail("맞는 팀의 이름이 없으므로 실패해야 합니다.");
        } catch (Exception e) {
            // PASS
        }
    }

    @Test
    public void testCreateTeamWhenTeamNameNotDuplicated() {

        // given
        TeamVO teamVO = TeamVO
                .builder()
                .name("testTeam")
                .build();

        // ok
        teamService.createTeamWhenTeamNameNotDuplicated(teamVO);

        Team team = findTeamByTeamNameOrGetNull(teamVO.getName());

        assertEquals(teamVO.getName(), team.getName());

        // fail
        try {
            teamService.createTeamWhenTeamNameNotDuplicated(teamVO);
            fail("팀 이름이 중복되어 저장이 정상적으로 되지 않아야 한다.");
        } catch (Exception e) {
            // PASS
        }

    }

    private Team findTeamByTeamNameOrGetNull(String teamName) {
        Predicate predicate = makeTeamNameLikePredicate(teamName);
        Optional<Team> findTeam = teamRepository.findOne(predicate);
        return findTeam.orElse(null);
    }

    private Predicate makeTeamNameLikePredicate(String teamName) {
        return QTeam.team.name.like(teamName);
    }

}