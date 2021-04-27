package com.assign.organization.service.member;

import com.assign.organization.domain.member.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<MemberVO> findMemberByKeyword(String keyword) {
        List<Member> findMembers = memberRepository.findByLikeAllField(keyword);

        log.info(findMembers.toString());

        return findMembers.stream().map(m -> MemberVO
                .builder()
                .name(m.getName())
                .duty(m.getDuty())
                .id(m.getId())
                .businessCall(m.getContact().getBusinessCall())
                .cellPhone(m.getContact().getCellPhone())
                .teamName(m.getTeam().getName())
                .build()).collect(Collectors.toList());
    }
}
