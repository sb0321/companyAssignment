package com.assign.organization.service.member;

import com.assign.organization.domain.member.MemberVO;

import java.util.List;

public interface MemberService {

    void deleteMemberById(Long id);

    List<MemberVO> findMemberByKeyword(String keyword);

}
