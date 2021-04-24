package com.assign.organization.service.member;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberDTO;
import com.assign.organization.domain.member.MemberVO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MemberService {

    MemberDTO findMemberById(Long id);

    boolean deleteMemberByName(String name);

    void deleteMemberById(Long id);

    void updateMember(MemberVO update);

    MemberDTO createMember(MemberVO newMember);

    Optional<Member> findMemberByIdEntity(Long id);
}
