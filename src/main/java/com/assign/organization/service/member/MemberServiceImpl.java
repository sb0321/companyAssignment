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
    public MemberDTO updateMemberAddress(Long id, Address address) {

        Optional<Member> findMember = memberRepository.findById(id);

        if (findMember.isEmpty()) {
            throw new NoResultException();
        }

        Member member = findMember.get();

        member.updateAddress(address);

        return MemberDTO
                .builder()
                .name(member.getName())
                .position(member.getPosition())
                .ranked(member.getRanked())
                .teamId(member.getTeam() == null ? null : member.getTeam().getId())
                .address(member.getAddress())
                .build();

    }

    @Override
    public MemberDTO updateMember(Long id, Position position, String name) {

        Optional<Member> findMember = memberRepository.findById(id);

        if (findMember.isEmpty()) {
            throw new NoResultException();
        }

        Member member = findMember.get();

        member.update(name, position);

        return MemberDTO
                .builder()
                .name(member.getName())
                .position(member.getPosition())
                .ranked(member.getRanked() == null ? null : member.getRanked())
                .teamId(member.getTeam() == null ? null : member.getTeam().getId())
                .address(member.getAddress())
                .build();
    }

    @Override
    public Optional<Member> findMemberByIdEntity(Long id) {
        return memberRepository.findById(id);
    }
}
