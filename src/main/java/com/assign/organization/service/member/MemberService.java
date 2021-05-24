package com.assign.organization.service.member;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.Team;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private static final String TEAM_DOES_NOT_EXIST = "없음";

    private final MemberRepository memberRepository;

    public List<MemberVO> findMembersContainsKeyword(String keyword) {
        List<Member> memberList = memberRepository.findMembersContainsKeyword(keyword);
        return convertMemberListToMemberVOList(memberList);
    }

    public List<MemberVO> convertMemberListToMemberVOList(List<Member> memberList) {
        List<MemberVO> memberVOList = new ArrayList<>();

        for (Member member : memberList) {
            Team team = member.getTeam();

            MemberVO vo = MemberVO
                    .builder()
                    .id(member.getId())
                    .name(member.getName())
                    .businessCall(member.getBusinessCall())
                    .cellPhone(member.getCellPhone())
                    .position(member.getPosition())
                    .teamName(team == null ? TEAM_DOES_NOT_EXIST : team.getName())
                    .enteredDate(member.getEnteredDate())
                    .duty(member.getDuty())
                    .build();

            memberVOList.add(vo);
        }

        return memberVOList;
    }

    public long countNameContains(String name) {
        return memberRepository.countNameContains(name);
    }

    public boolean checkMemberIdDuplication(Long memberId) {
        return memberRepository.checkMemberIdDuplication(memberId);
    }
}
