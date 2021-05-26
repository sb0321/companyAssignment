package com.assign.organization.service.member;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.exception.MemberIdDuplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberVO> findMembersContainsKeyword(String keyword) {
        List<Member> membersContainsKeyword = memberRepository.findMembersContainsKeyword(keyword);
        return convertMemberListToMemberVOList(membersContainsKeyword);
    }

    private List<MemberVO> convertMemberListToMemberVOList(List<Member> memberList) {
        return memberList.stream().map(MemberVO::from).collect(Collectors.toList());
    }

    public long countFirstNameContains(String firstName) {
        return memberRepository.countFirstNameContains(firstName);
    }

    public void checkMemberIdDuplication(Long memberId) {
        boolean duplicated = memberRepository.checkMemberIdDuplication(memberId);
        if (duplicated) {
            throw new MemberIdDuplicationException();
        }
    }
}
