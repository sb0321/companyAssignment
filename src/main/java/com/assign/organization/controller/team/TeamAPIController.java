package com.assign.organization.controller.team;

import com.assign.organization.domain.member.MemberDTO;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.team.TeamDTO;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamAPIController {

    private final TeamService teamService;

    @PostMapping("")
    public void createTeam(TeamVO newTeam) {

        log.info(newTeam.toString());
        teamService.createTeam(newTeam);
    }

    @GetMapping("/{id}")
    public TeamVO getTeam(@PathVariable(name = "id") Long id) {

        TeamDTO findTeam = teamService.findTeamById(id);

        MemberDTO teamLeaderDTO = findTeam.getTeamLeader();

        log.info(teamLeaderDTO.toString());

        MemberVO teamLeaderVO = MemberVO
                .builder()
                .id(teamLeaderDTO.getId())
                .name(teamLeaderDTO.getName())
                .businessCall(teamLeaderDTO.getAddress().getBusinessCall())
                .cellPhone(teamLeaderDTO.getAddress().getCellPhone())
                .build();

        log.info(teamLeaderVO.toString());

        Set<MemberVO> members = new HashSet<>();

        for (MemberDTO member : findTeam.getMembers()) {

            log.info(member.toString());

            MemberVO m = MemberVO
                    .builder()
                    .id(member.getId())
                    .name(member.getName())
                    .businessCall(member.getAddress().getBusinessCall())
                    .cellPhone(member.getAddress().getCellPhone())
                    .build();

            members.add(m);
        }

        log.info(members.toString());

        TeamVO vo = TeamVO
                .builder()
                .members(members)
                .teamLeader(teamLeaderVO)
                .id(findTeam.getId())
                .name(findTeam.getName())
                .build();

        log.info(vo.toString());

        return vo;
    }

}