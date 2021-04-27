package com.assign.organization.controller.team;

import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

}
