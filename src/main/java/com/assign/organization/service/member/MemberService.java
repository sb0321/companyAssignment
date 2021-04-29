package com.assign.organization.service.member;

import com.assign.organization.domain.member.*;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberExpression;
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
        Predicate predicate = makeNameContainsPredicate(name);
        return memberRepository.count(predicate);
    }

    private Predicate makeNameContainsPredicate(String name) {
        QMember member = QMember.member;
        return member.name.like(name + "%");
    }

    public Member findMemberById(Long id) {
        Predicate predicate = makeMemberIdSearchPredicate(id);
        Optional<Member> findMember = memberRepository.findOne(predicate);

        if(!findMember.isPresent()) {
            throw new NoResultException();
        }

        return findMember.get();
    }

    private Predicate makeMemberIdSearchPredicate(Long id) {
        return QMember.member.id.eq(id);
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
