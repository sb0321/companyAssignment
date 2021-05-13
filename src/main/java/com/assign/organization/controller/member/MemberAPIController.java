package com.assign.organization.controller.member;

import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberAPIController {

    private final MemberService memberService;

    @GetMapping("")
    public List<MemberVO> searchKeywordMemberVOList(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return memberService.findMembersContainsKeyword(keyword);
    }

}
