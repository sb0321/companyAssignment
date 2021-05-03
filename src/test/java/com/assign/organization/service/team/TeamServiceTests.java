package com.assign.organization.service.team;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Contact;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.domain.team.TeamVO;
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
    public void testInsertTeams() {

        // given
        List<Team> teamList = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            teamList.add(new Team("team" + i));
        }

        // when
        teamService.insertTeams(teamList);

        // then
        List<Team> findTeamList = teamRepository.findAllTeamsOrderByTeamName();

        for (int i = 0; i < teamList.size(); i++) {
            assertEquals(teamList.get(i).getName(), findTeamList.get(i).getName());
        }

    }

    @Test
    public void testInsertTeamsFromTeamVOList() {

        // given
        List<TeamVO> teamVOList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            TeamVO teamVO = TeamVO
                    .builder()
                    .name("test" + i)
                    .build();

            teamVOList.add(teamVO);
        }

        // duplicatedTeamName
        teamVOList.add(TeamVO.builder().name("test1").build());

        // when
        teamService.insertTeamsFromTeamVOList(teamVOList);

        // then
        for(int i = 0; i < 5; i++) {
            Optional<Team> findTeam = teamRepository.findByTeamName("test" + i);
            assertTrue(findTeam.isPresent());
            assertEquals(teamVOList.get(i).getName(), findTeam.get().getName());
        }

        // then 중복된 이름은 없이 만들어져야 함
        assertEquals(teamVOList.size() - 1, teamRepository.findAllTeamsOrderByTeamName().size());
    }

    @Test
    public void testExtractTeamVOListFromCSVMemberVOList() {

        // given
        List<CSVMemberVO> csvMemberVOList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            CSVMemberVO csvMemberVO = CSVMemberVO
                    .builder()
                    .id((long)i)
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
        List<TeamVO> teamVOList = teamService.extractTeamVOListFromCSVMemberVOList(csvMemberVOList);

        log.info(teamVOList.toString());

        for (int i = 0; i < teamVOList.size(); i++) {
            assertEquals(csvMemberVOList.get(i).getTeamName(), teamVOList.get(i).getName());
        }

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

        List<TeamVO> teamVOList = teamService.findAllTeamListOrderByTeamNameDesc();
        log.info(teamVOList.toString());
    }

    private void addMembersToTeam(Team team, List<Member> memberList) {
        memberList.forEach(team::addTeamMember);
    }

    private List<Member> makeMemberList(int startIdIdx) {
        List<Member> memberList = new ArrayList<>();

        for (int i = startIdIdx * 4; i < startIdIdx + 4; i++) {

            Contact contact = Contact
                    .builder()
                    .cellPhone("010-0000-00" + i)
                    .businessCall("10" + i)
                    .build();

            Member member = Member
                    .builder()
                    .id((long) i)
                    .name("member" + i)
                    .duty("사원")
                    .position("팀원")
                    .contact(contact)
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