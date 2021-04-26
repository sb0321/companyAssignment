package com.assign.organization.controller.main;

import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final MemberService memberService;

    @GetMapping("")
    public String search(Model model, HttpServletRequest request) {

        String keyword = request.getParameter("keyword");

        if (keyword == null) {
            keyword = "";
        }

        log.info(keyword);

        List<MemberVO> findMember = memberService.findMemberByKeyword(keyword);

        model.addAttribute("memberList", findMember);

        return "search";
    }


}
