package com.assign.organization.service.member;

import com.assign.organization.domain.member.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDTO findMemberById(Long id) {

        Optional<Member> findMember = memberRepository.findById(id);

        if (findMember.isEmpty()) {
            return null;
        }

        Member member = findMember.get();

        return MemberDTO
                .builder()
                .name(member.getName())
                .ranked(member.getRanked())
                .position(member.getPosition())
                .teamId(member.getTeam().getId())
                .build();
    }

    @Override
    public boolean deleteMemberByName(String name) {

        int deleteCount = memberRepository.deleteByName(name);

        return deleteCount > 0;
    }

    @Override
    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public void updateMember(MemberVO update) {

        Optional<Member> findMember = memberRepository.findById(update.getId());

        if (findMember.isEmpty()) {
            throw new NoResultException();
        }

        Member member = findMember.get();

        Address newAddress = Address
                .builder()
                .cellPhone(update.getCellPhone() == null ?
                        member.getAddress().getCellPhone() : update.getCellPhone())
                .businessCall(update.getBusinessCall() == null ?
                        member.getAddress().getCellPhone() : update.getBusinessCall())
                .build();

        MemberDTO dto = MemberDTO
                .builder()
                .name(update.getName() == null ? member.getName() : update.getName())
                .ranked(update.getRanked() == null ? member.getRanked() : update.getRanked())
                .address(newAddress)
                .position(member.getPosition())
                .build();

        member.update(dto);

    }

    @Override
    public MemberDTO createMember(MemberVO newMember) {

        Address address = Address
                .builder()
                .businessCall(newMember.getBusinessCall())
                .cellPhone(newMember.getCellPhone())
                .build();

        Member member = Member
                .builder()
                .name(newMember.getName())
                .ranked(newMember.getRanked())
                .address(address)
                .build();

        Member savedMember = memberRepository.save(member);

        return MemberDTO
                .builder()
                .name(savedMember.getName())
                .teamId(savedMember.getTeam() == null ? null : savedMember.getTeam().getId())
                .ranked(savedMember.getRanked())
                .address(savedMember.getAddress())
                .position(savedMember.getPosition())
                .build();
    }

    @Override
    public Optional<Member> findMemberByIdEntity(Long id) {
        return memberRepository.findById(id);
    }
}
