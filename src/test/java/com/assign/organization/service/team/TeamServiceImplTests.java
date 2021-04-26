package com.assign.organization.service.team;

import com.assign.organization.domain.member.Contact;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamDTO;
import com.assign.organization.domain.team.TeamRepository;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.NoResultException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class TeamServiceImplTests {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private TeamServiceImpl teamService;

    private static final Long TEAM_ID = 1L;

    private static final String TEAM_NAME = "TEAM";
    private static final String NOT_EXIST_TEAM_NAME = "NOT";
    private static final String ALREADY_EXIST_TEAM_NAME = "EXIST";

    private static final Long MEMBER_ID = 1L;

    private static final String DUTY = "팀장";

    @BeforeEach
    public void init() {

        Team team = Team
                .builder()
                .name(TEAM_NAME)
                .build();

        Contact contact = Contact
                .builder()
                .cellPhone("010-1111-1111")
                .businessCall("1111")
                .build();

        Member member = Member
                .builder()
                .name("member")
                .position("매니저")
                .duty(DUTY)
                .contact(contact)
                .build();

        team.changeTeamLeader(member);

        Mockito.when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));

        Mockito.when(teamRepository.deleteByName(TEAM_NAME)).thenReturn(1);
        Mockito.when(teamRepository.deleteByName(NOT_EXIST_TEAM_NAME)).thenReturn(0);

        Mockito.when(teamRepository.findNameExist(ALREADY_EXIST_TEAM_NAME)).thenReturn(1);
        Mockito.when(teamRepository.findNameExist(NOT_EXIST_TEAM_NAME)).thenReturn(0);

        Mockito.when(memberService.findMemberByIdEntity(MEMBER_ID)).thenReturn(Optional.of(member));

    }

    @Test
    public void testFindTeamById() {

        // when
        TeamDTO findTeam = teamService.findTeamById(TEAM_ID);

        log.info(findTeam.toString());

        assertEquals(TEAM_NAME, findTeam.getName());
        assertEquals(DUTY, findTeam.getTeamLeader().getDuty());

    }

    @Test
    public void testDeleteTeamByName() {

        // when
        boolean deleted = teamService.deleteTeamByName(TEAM_NAME);

        // then
        assertTrue(deleted);

        // when
        boolean notDeleted = teamService.deleteTeamByName(NOT_EXIST_TEAM_NAME);

        // then
        assertFalse(notDeleted);

    }

    @Test
    public void testCreateTeam() {

        // given
        String teamName = "testTeam";

        TeamVO vo = TeamVO
                .builder()
                .name(teamName)
                .build();

        Team team = Team
                .builder()
                .name(vo.getName())
                .build();

        // when
        Mockito.when(teamRepository.save(any())).thenReturn(team);

        Team savedTeam = teamService.createTeam(vo);

        // then
        assertEquals(teamName, savedTeam.getName());
    }

}