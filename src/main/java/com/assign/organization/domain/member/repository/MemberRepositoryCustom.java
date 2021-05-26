package com.assign.organization.domain.member.repository;

import com.assign.organization.domain.member.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    long countFirstNameContains(String name);

    List<Member> findMembersContainsKeyword(String keyword);

    boolean checkMemberIdDuplication(Long memberId);

    boolean checkBusinessCallDuplication(String businessCall);
}
