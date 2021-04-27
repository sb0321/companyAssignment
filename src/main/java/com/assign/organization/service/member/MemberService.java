package com.assign.organization.service.member;

import com.assign.organization.domain.member.MemberVO;

import java.util.List;

public interface MemberService {

    List<MemberVO> findMemberByKeyword(String keyword);

}
