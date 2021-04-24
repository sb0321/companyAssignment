package com.assign.organization.controller.main;

import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final TeamService teamService;
    private final MemberService memberService;

    @GetMapping("/")
    public String hello(Model model) {

        List<TeamVO> teamList = teamService.getTeamList();

        log.info(teamList.toString());

        model.addAttribute("teamList", teamList);

        return "hello";
    }

    @GetMapping("/address")
    public String address(Model model) {



        return "contact";
    }

}
