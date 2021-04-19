package com.assign.organization.domain.team;

import com.assign.organization.config.JpaDatabaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Import(JpaDatabaseConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void testRead() {

        // given
        Team team = Team
                .builder()
                .name("test")
                .build();

        // when
        teamRepository.save(team);
        teamRepository.flush();

        Optional<Team> findTeam = teamRepository.findById(team.getId());

        // then
        assertFalse(findTeam.isEmpty());
        assertEquals(team, findTeam.get());

    }

    @Test
    public void testInsert() {

        // given
        Team team = Team
                .builder()
                .name("testTeam")
                .build();

        // when
        teamRepository.save(team);
        teamRepository.flush();

        // then
        Team findTeam = teamRepository.getOne(team.getId());

        assertEquals(team,findTeam);
    }

    @Test
    public void testUpdate() {

        // given
        String beforeTeamName = "beforeTeam";

        Team team = Team
                .builder()
                .name(beforeTeamName)
                .build();

        // when
        teamRepository.save(team);
        teamRepository.flush();

        Team findTeam = teamRepository.getOne(team.getId());

        String afterTeamName = "afterTeam";
        findTeam.updateTeamName(afterTeamName);

        teamRepository.flush();

        // then
        assertNotEquals(beforeTeamName, findTeam.getName());
    }

    @Test
    public void testDeleteById() {

        // given
        Team team = Team
                .builder()
                .name("test")
                .build();

        teamRepository.save(team);
        teamRepository.flush();

        // when
        teamRepository.deleteById(team.getId());

        // then
        Optional<Team> deletedTeam = teamRepository.findById(team.getId());

        assertTrue(deletedTeam.isEmpty());
    }

}
