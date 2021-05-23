package com.assign.organization.controller.member;

import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberAPIController {

    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<List<MemberVO>> searchKeywordMemberVOList(
            @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword == null) {
            keyword = "";
        }

        List<MemberVO> findMembers = memberService.findMembersContainsKeyword(keyword);
        return ResponseEntity.of(Optional.of(findMembers));
    }

}
