package com.assign.organization.service.member;

import com.assign.organization.domain.member.*;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.utils.NameGenerator;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberById(Long id) {

        Optional<Member> findMember = memberRepository.findById(id);

        if (!findMember.isPresent()) {
            throw new NoResultException();
        }

        return findMember.get();
    }

    @Transactional
    public void insertMembersFromCSVMemberVOList(List<CSVMemberVO> csvMemberVOList) {
        List<MemberVO> memberVOList = convertCSVMemberVOListToMemberVOList(csvMemberVOList);
        createMembersFromMemberVOList(memberVOList);
    }

    public void createMemberFromMemberVO(MemberVO memberVO) {
        Member newMember = convertMemberVOToMemberEntity(memberVO);
        memberRepository.save(newMember);
    }

    private void createMembersFromMemberVOList(List<MemberVO> memberVOList) {
        for (MemberVO memberVO : memberVOList) {
            String convertedName = generateNewMemberNameIfDuplicated(memberVO.getName());
            memberVO.setName(convertedName);
            createMemberFromMemberVO(memberVO);
        }
    }

    private List<MemberVO> convertCSVMemberVOListToMemberVOList(List<CSVMemberVO> csvMemberVOList) {
        List<MemberVO> convertedList = new ArrayList<>();

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {
            MemberVO converted = convertCSVMemberVOToMemberVO(csvMemberVO);
            convertedList.add(converted);
        }
        return convertedList;
    }

    private String generateNewMemberNameIfDuplicated(String name) {
        long nameDuplicationCount = memberRepository.countNameContains(name);
        return NameGenerator.generateNameWhenDuplication(name, nameDuplicationCount);
    }

    private MemberVO convertCSVMemberVOToMemberVO(CSVMemberVO csvMemberVO) {
        return MemberVO
                .builder()
                .id(csvMemberVO.getId())
                .name(csvMemberVO.getName())
                .businessCall(csvMemberVO.getBusinessCall())
                .cellPhone(csvMemberVO.getCellPhone())
                .position(csvMemberVO.getPosition())
                .duty(csvMemberVO.getDuty())
                .build();
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
