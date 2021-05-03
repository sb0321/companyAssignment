package com.assign.organization.domain.team;

import com.assign.organization.domain.team.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeamRepositoryTests {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    @Transactional
    public void testFindByTeamName() {

        // given
        Team team = new Team("test");

        teamRepository.save(team);

        // when
        Optional<Team> findTeam = teamRepository.findByTeamName("test");

        // then
        assertTrue(findTeam.isPresent());

        // when
        Optional<Team> notFindTeam = teamRepository.findByTeamName("no");

        // then
        assertFalse(notFindTeam.isPresent());
    }

    @Test
    @Transactional
    public void testCountTeamNameDuplication() {

        // given
        Team team = new Team("test");

        teamRepository.save(team);

        // when
        long duplicationCount = teamRepository.countTeamNameDuplication("test");

        // then
        assertEquals(1, duplicationCount);

        // when
        duplicationCount = teamRepository.countTeamNameDuplication("notDuplicated");

        // then
        assertEquals(0, duplicationCount);
    }

    @Test
    @Transactional
    public void testFindAllTeams() {

        // given
        List<Team> teamList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            teamList.add(new Team("team" + i));
        }

        Iterable<Team> teams = teamRepository.saveAll(teamList);
        List<Team> savedTeams = new ArrayList<>();
        teams.forEach(savedTeams::add);

        List<Team> findTeamList = teamRepository.findAllTeamsOrderByTeamName();

        for (int i = 0; i < savedTeams.size(); i++) {
            assertEquals(savedTeams.get(i).getName(), findTeamList.get(i).getName());
        }

    }

}
