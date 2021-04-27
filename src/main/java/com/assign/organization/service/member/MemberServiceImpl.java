package com.assign.organization.service.member;

import com.assign.organization.domain.member.*;
import com.assign.organization.service.team.TeamService;
import com.assign.organization.utils.NameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
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
    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }

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
