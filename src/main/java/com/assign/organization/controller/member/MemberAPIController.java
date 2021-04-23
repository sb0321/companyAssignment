package com.assign.organization.controller.member;

import com.assign.organization.domain.member.MemberDTO;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberAPIController {

    private final MemberService memberService;

    @PostMapping("")
    public Long createMember(MemberVO memberVO) {

        MemberDTO member = memberService.createMember(memberVO);

        return member.getId();
    }

    @GetMapping("/{id}")
    public MemberVO getMember(@PathVariable(name = "id") Long id) {

        log.info(String.valueOf(id));
        MemberDTO findMember = memberService.findMemberById(id);

        MemberVO vo = MemberVO
                .builder()
                .id(id)
                .businessCall(findMember.getAddress().getBusinessCall())
                .cellPhone(findMember.getAddress().getCellPhone())
                .duty(findMember.getDuty())
                .build();

        return vo;
    }

    @PutMapping("/{id}")
    public void updateMember(@PathVariable(name = "id") Long id, MemberVO vo) {

        log.info(vo.toString());

        vo.setId(id);
        memberService.updateMember(vo);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable(name = "id") Long id) {

        log.info(String.valueOf(id));
        memberService.deleteMemberById(id);
    }

}
