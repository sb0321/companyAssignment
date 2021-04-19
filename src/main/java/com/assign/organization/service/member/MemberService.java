package com.assign.organization.service.member;

import com.assign.organization.domain.member.Address;
import com.assign.organization.domain.member.MemberDTO;
import com.assign.organization.domain.member.Position;

public interface MemberService {

    MemberDTO findMemberById(Long id);

    boolean deleteMemberByName(String name);

    MemberDTO updateMemberAddress(Long id, Address address);

    MemberDTO updateMember(Long id, Position position, String name);
}
