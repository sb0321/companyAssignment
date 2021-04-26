package com.assign.organization.service.member;

import com.assign.organization.domain.member.CSVMemberDTO;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberDTO;
import com.assign.organization.domain.member.MemberVO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    MemberDTO findMemberById(Long id);

    boolean deleteMemberByName(String name);

    void deleteMemberById(Long id);

    void updateMember(MemberVO update);

    List<Member> initMembers(List<CSVMemberDTO> csvMemberDTOList);

    List<MemberVO> findMemberByKeyword(String keyword);

    MemberDTO createMember(MemberVO newMember);

    Optional<Member> findMemberByIdEntity(Long id);
}
