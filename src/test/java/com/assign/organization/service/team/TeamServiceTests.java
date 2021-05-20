package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.LinkedList;
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

    @Mock
    MemberService memberService;

    @InjectMocks
    TeamService teamService;

    @Test
    void testFindAllTeamListOrderByTeamNameDesc() {

        List<Team> result = new ArrayList<>();

        for (int i = 5; i >= 0; i--) {
            Team team = new Team("test" + i);

            Member member = Member
                    .builder()
                    .firstName("name" + i)
                    .lastName("last" + i)
                    .position("pos" + i)
                    .duty("duty")
                    .cellPhone("010-0000-000" + i)
                    .businessCall("100" + i)
                    .build();

            team.addMember(member);
            result.add(team);
        }

        when(teamRepository.findAllTeamsOrderByTeamName()).thenReturn(result);
        when(memberService.convertMemberListToMemberVOList(anyList())).thenAnswer((Answer<List<MemberVO>>) invocation -> {
            List<Member> memberList = invocation.getArgument(0);
            return convert(memberList);
        });

        List<TeamVO> findTeamList = teamService.findAllTeamListOrderByTeamNameDesc();

        verify(teamRepository, times(1)).findAllTeamsOrderByTeamName();

        log.info(findTeamList.toString());
    }

    List<MemberVO> convert(List<Member> memberList) {
        List<MemberVO> convert = new LinkedList<>();

        for (Member member : memberList) {
            MemberVO vo = MemberVO
                    .builder()
                    .id(member.getId())
                    .name(member.getName())
                    .duty(member.getDuty())
                    .teamName(member.getTeam() == null ? "없음" : member.getTeam().getName())
                    .cellPhone(member.getCellPhone())
                    .businessCall(member.getBusinessCall())
                    .position(member.getPosition())
                    .build();

            convert.add(vo);
        }
        return convert;
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