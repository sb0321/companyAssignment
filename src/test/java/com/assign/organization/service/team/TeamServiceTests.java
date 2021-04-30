package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.QTeam;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.domain.team.TeamVO;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Transactional
    public void testFindAllTeamListOrderByTeamNameDesc() {
        List<Team> teamList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Team team = Team
                    .builder()
                    .name("team" + i)
                    .build();
            List<Member> memberList = makeMemberList(i);
            addMembersToTeam(team, memberList);
            teamList.add(team);
        }

        teamRepository.saveAll(teamList);

        List<Team> teams = teamService.findAllTeamListOrderByTeamNameDesc();
        log.info(teams.toString());
    }

    private void addMembersToTeam(Team team, List<Member> memberList) {
        memberList.forEach(team::addTeamMember);
    }

    private List<Member> makeMemberList(int startIdIdx) {
        List<Member> memberList = new ArrayList<>();

        for (int i = startIdIdx * 4; i < startIdIdx + 4; i++) {
            Member member = Member
                    .builder()
                    .id((long) i)
                    .name("member" + i)
                    .duty("사원")
                    .position("팀원")
                    .build();

            memberList.add(member);
        }

        return memberList;
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

}