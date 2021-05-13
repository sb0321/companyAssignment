package com.assign.organization.controller.team;

import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamAPIController {

    private final TeamService teamService;

    @GetMapping("")
    public List<TeamVO> getTeamsWithMembers() {
        return teamService.findAllTeamListOrderByTeamNameDesc();
    }


}
