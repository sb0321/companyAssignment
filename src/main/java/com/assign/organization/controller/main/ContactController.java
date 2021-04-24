package com.assign.organization.controller.main;

import com.assign.organization.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController {

    private final MemberService memberService;

    @GetMapping("")
    public String contact(Model model, HttpServletRequest request) {

        String part = request.getParameter("name");


        return "contact";
    }


}
