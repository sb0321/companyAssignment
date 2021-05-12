package com.assign.organization.service.team;

import com.assign.organization.domain.contact.Contact;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.domain.team.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TeamServiceTests {

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamService teamService;

    @Test
    void testFindAllTeamListOrderByTeamNameDesc() {

        List<Team> result = new ArrayList<>();

        for (int i = 5; i >= 0; i--) {
            Team team = new Team("test" + i);

            Member member = Member
                    .builder()
                    .name("name" + i)
                    .position("pos" + i)
                    .duty("duty")
                    .contact(new Contact("010-0000-000" + i, "100" + i))
                    .build();

            member.setTeam(team);

            result.add(team);
        }

        when(teamRepository.findAllTeamsOrderByTeamName()).thenReturn(result);

        List<TeamVO> findTeamList = teamService.findAllTeamListOrderByTeamNameDesc();

        verify(teamRepository, times(1)).findAllTeamsOrderByTeamName();

        log.info(findTeamList.toString());
    }

    @Test
    void testFindTeamByTeamName() {

        String teamName = "test";

        Team team = new Team(teamName);

        when(teamRepository.findByTeamName(any())).thenReturn(Optional.of(team));

        Optional<Team> findTeam = teamService.findTeamByTeamName(teamName);

        verify(teamRepository, times(1)).findByTeamName(anyString());

        assertTrue(findTeam.isPresent());
        assertEquals(teamName, findTeam.get().getName());
    }


}