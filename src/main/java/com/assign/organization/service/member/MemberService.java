package com.assign.organization.service.member;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Contact;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.Team;
import com.assign.organization.exception.CSVFileInvalidException;
import com.assign.organization.service.team.TeamService;
import com.assign.organization.utils.CSVReader;
import com.assign.organization.utils.NameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private static final String TEAM_DOES_NOT_EXIST = "없음";

    private final MemberRepository memberRepository;

    private final TeamService teamService;

    public List<MemberVO> findMembersContainsKeyword(String keyword) {
        List<Member> memberList = memberRepository.findMembersContainsKeyword(keyword);
        return convertMemberListToMemberVOList(memberList);
    }

    @Transactional
    public void insertMembersFromCSVFile(String csvFilePath) throws CSVFileInvalidException {

        List<CSVMemberVO> csvMemberVOList = CSVReader.readCSVFile(csvFilePath);

        Map<String, Team> teams = extractTeamsFromCSVMemberVOList(csvMemberVOList);

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {

            Contact contact = new Contact(csvMemberVO.getCellPhone(), csvMemberVO.getBusinessCall());

            String newName = generateNewMemberNameIfDuplicated(csvMemberVO.getName());

            Member member = Member
                    .builder()
                    .name(newName)
                    .position(csvMemberVO.getPosition())
                    .duty(csvMemberVO.getDuty())
                    .contact(contact)
                    .build();

            member.changeTeam(teams.get(csvMemberVO.getTeamName()));

            memberRepository.save(member);
        }
    }

    private List<MemberVO> convertMemberListToMemberVOList(List<Member> memberList) {
        List<MemberVO> memberVOList = new ArrayList<>();

        for (Member member : memberList) {

            Team team = member.getTeam();

            MemberVO vo = MemberVO
                    .builder()
                    .id(member.getId())
                    .name(member.getName())
                    .businessCall(member.getContact().getBusinessCall())
                    .cellPhone(member.getContact().getCellPhone())
                    .position(member.getPosition())
                    .teamName(team == null ? TEAM_DOES_NOT_EXIST : team.getName())
                    .duty(member.getDuty())
                    .build();

            memberVOList.add(vo);
        }

        return memberVOList;
    }

    private Map<String, Team> extractTeamsFromCSVMemberVOList(List<CSVMemberVO> csvMemberVOList) {
        Map<String, Team> teams = new HashMap<>();
        for (CSVMemberVO csvMemberVO : csvMemberVOList) {
            Team team = findTeamOrMakeNewTeam(csvMemberVO.getTeamName());
            teams.putIfAbsent(csvMemberVO.getTeamName(), team);
        }

        return teams;
    }

    private Team findTeamOrMakeNewTeam(String teamName) {
        Optional<Team> findTeam = teamService.findTeamByTeamName(teamName);
        return findTeam.orElseGet(() -> new Team(teamName));
    }

    private String generateNewMemberNameIfDuplicated(String name) {
        long duplicationCount = memberRepository.countNameContains(name);
        return NameGenerator.generateNameWhenDuplication(name, duplicationCount);
    }
}
