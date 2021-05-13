package com.assign.organization.service.member;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.member.repository.MemberRepository;
import com.assign.organization.domain.team.Team;
import com.assign.organization.exception.InvalidCSVFileException;
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

    private List<MemberVO> convertMemberListToMemberVOList(List<Member> memberList) {
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
                    .duty(member.getDuty())
                    .build();

            memberVOList.add(vo);
        }

        return memberVOList;
    }

    @Transactional
    public void insertMembersFromCSVFile(String csvFilePath) throws InvalidCSVFileException {

        List<CSVMemberVO> csvMemberVOList = CSVReader.readCSVFile(csvFilePath);

        Map<String, Team> teams = extractTeamsFromCSVMemberVOList(csvMemberVOList);

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {

            String newFirstName = generateNewMemberNameIfDuplicated(csvMemberVO);
            log.info(newFirstName);

            Member member = Member
                    .builder()
                    .id(csvMemberVO.getMemberId())
                    .firstName(newFirstName)
                    .lastName(csvMemberVO.getLastName())
                    .enteredDate(csvMemberVO.getEnteredDate())
                    .position(csvMemberVO.getPosition())
                    .duty(csvMemberVO.getDuty())
                    .businessCall(csvMemberVO.getBusinessCall())
                    .cellPhone(csvMemberVO.getCellPhone())
                    .build();

            member.setTeam(teams.get(csvMemberVO.getTeamName()));
            memberRepository.save(member);
        }
    }

    private String generateNewMemberNameIfDuplicated(CSVMemberVO vo) {
        String name = String.join("", vo.getLastName(), vo.getFirstName());
        long duplicationCount = memberRepository.countNameContains(name);
        return NameGenerator.generateNameWhenDuplication(vo.getFirstName(), duplicationCount);
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


}
