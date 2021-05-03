package com.assign.organization.service.member;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Contact;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.Team;
import com.assign.organization.service.team.TeamService;
import com.assign.organization.utils.NameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final TeamService teamService;

    public List<MemberVO> findMembersContainsKeyword(String keyword) {
        List<Member> memberList = memberRepository.findMembersContainsKeyword(keyword);
        return convertMemberListToMemberVOList(memberList);
    }

    private List<MemberVO> convertMemberListToMemberVOList(List<Member> memberList) {

        List<MemberVO> memberVOList = new ArrayList<>();

        for (Member member : memberList) {
            MemberVO vo = MemberVO
                    .builder()
                    .id(member.getId())
                    .name(member.getName())
                    .businessCall(member.getContact().getBusinessCall())
                    .cellPhone(member.getContact().getCellPhone())
                    .position(member.getPosition())
                    .teamName(member.getTeam().getName())
                    .duty(member.getDuty())
                    .build();

            memberVOList.add(vo);
        }

        return memberVOList;
    }

    public Member findMemberById(Long id) {

        Optional<Member> findMember = memberRepository.findById(id);

        if (!findMember.isPresent()) {
            throw new NoResultException();
        }

        return findMember.get();
    }

    @Transactional
    public void insertMembersFromCSVMemberVOList(List<CSVMemberVO> csvMemberVOList) {
        Collection<Team> teams = connectMembersToTeamAndGetTeamCollection(csvMemberVOList);
        teamService.insertTeams(teams);
    }

    private Collection<Team> connectMembersToTeamAndGetTeamCollection(List<CSVMemberVO> csvMemberVOList) {

        Map<String, Team> teams = new HashMap<>();
        Map<String, Integer> nameDuplication = new HashMap<>();

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {
            MemberVO memberVO = convertCSVMemberVOToMemberVO(csvMemberVO);

            int nameDuplicatedCount = getNameDuplicationCount(nameDuplication, memberVO.getName());
            String newName = generateNewMemberNameIfDuplicated(memberVO.getName(), nameDuplicatedCount);
            memberVO.setName(newName);

            Member member = convertMemberVOToMemberEntity(memberVO);

            String teamName = csvMemberVO.getTeamName();

            teams.putIfAbsent(teamName, Team.builder().name(teamName).build());
            teams.get(teamName).addTeamMember(member);
        }

        return teams.values();
    }

    private int getNameDuplicationCount(Map<String, Integer> nameDuplication, String name) {

        if (!nameDuplication.containsKey(name)) {
            nameDuplication.put(name, -1);
        }

        nameDuplication.replace(name, nameDuplication.get(name) + 1);

        return nameDuplication.get(name);
    }

    private String generateNewMemberNameIfDuplicated(String name, int duplicationCount) {
        return NameGenerator.generateNameWhenDuplication(name, duplicationCount);
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
