package com.assign.organization.service.member;

import com.assign.organization.domain.member.*;

import java.util.Optional;

public interface MemberService {

    MemberDTO findMemberById(Long id);

    boolean deleteMemberByName(String name);

    void deleteMemberById(Long id);

    void updateMember(MemberVO update);

    MemberDTO createMember(MemberVO newMember);

    Optional<Member> findMemberByIdEntity(Long id);
}
