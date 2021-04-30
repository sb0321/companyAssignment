package com.assign.organization.service.member;

import com.assign.organization.domain.member.*;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long countMemberNameDuplication(String name) {
        return memberRepository.countNameContains(name);
    }


    public Member findMemberById(Long id) {

        Optional<Member> findMember = memberRepository.findById(id);

        if (!findMember.isPresent()) {
            throw new NoResultException();
        }

        return findMember.get();
    }

    @Transactional
    public void createMemberFromMemberVO(MemberVO memberVO) {
        Member newMember = convertMemberVOToMemberEntity(memberVO);
        memberRepository.save(newMember);
    }

    private Member convertMemberVOToMemberEntity(MemberVO memberVO) {

        Contact contact = makeContactFromMemberVO(memberVO);

        return
                Member
                        .builder()
                        .id(memberVO.getId())
                        .name(memberVO.getName())
                        .duty(memberVO.getDuty())
                        .position(memberVO.getPosition())
                        .contact(contact)
                        .build();
    }

    private Contact makeContactFromMemberVO(MemberVO vo) {
        return new Contact(vo.getCellPhone(), vo.getBusinessCall());
    }

}
