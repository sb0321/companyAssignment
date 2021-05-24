package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.member.MemberVO;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.exception.InvalidCSVFileException;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.utils.CSVMemberVO;
import com.assign.organization.utils.CSVReader;
import com.assign.organization.utils.NameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberService memberService;

    @Value(value = "${insert.batch.size}")
    private int BATCH_SIZE;

    @Transactional
    public void insertMembersFromCSVFile(String csvFilePath) throws InvalidCSVFileException {
        CSVReader.setCSVFile(csvFilePath);
        while (true) {
            List<CSVMemberVO> csvMemberVOList = CSVReader.readCSVMemberVOList(BATCH_SIZE);

            if (csvMemberVOList.isEmpty()) {
                break;
            }
            saveMembersAndTeams(csvMemberVOList);
        }
        CSVReader.close();
    }

    private void saveMembersAndTeams(List<CSVMemberVO> csvMemberVOList) throws InvalidCSVFileException {
        Map<String, Team> teams = new HashMap<>();

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {

            if (memberService.checkMemberIdDuplication(csvMemberVO.getMemberId())) {
                throw new InvalidCSVFileException("중복되는 사번이 있습니다. 사번:" + csvMemberVO.getMemberId() +
                        " 이름:" + csvMemberVO.getLastName() + csvMemberVO.getFirstName());
            }

            teams.putIfAbsent(csvMemberVO.getTeamName(), findTeamOrMakeNewTeam(csvMemberVO.getTeamName()));

            Member member = CSVMemberVOToMember(csvMemberVO);
            member.setTeam(teams.get(csvMemberVO.getTeamName()));
        }

        teamRepository.saveAll(teams.values());
    }

    private Member CSVMemberVOToMember(CSVMemberVO csvMemberVO) {

        String newFirstName = generateNewMemberNameIfDuplicated(csvMemberVO);

        return Member
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
    }

    private String generateNewMemberNameIfDuplicated(CSVMemberVO vo) {
        String name = String.join("", vo.getLastName(), vo.getFirstName());
        long duplicationCount = memberService.countNameContains(name);
        return NameGenerator.generateNameWhenDuplication(vo.getFirstName(), duplicationCount);
    }

    private Team findTeamOrMakeNewTeam(String teamName) {
        Optional<Team> findTeam = teamRepository.findByTeamName(teamName);
        return findTeam.orElseGet(() -> new Team(teamName));
    }

    public List<TeamVO> findAllTeamListOrderByTeamNameDesc() {
        List<Team> teamList = teamRepository.findAllTeamsOrderByTeamName();
        return Collections.unmodifiableList(convertTeamListToTeamVOList(teamList));
    }

    private List<TeamVO> convertTeamListToTeamVOList(List<Team> teamList) {
        List<TeamVO> teamVOList = new ArrayList<>();

        for (Team team : teamList) {
            List<MemberVO> memberVOList = memberService.convertMemberListToMemberVOList(team.getMembers());
            TeamVO vo = new TeamVO(team.getId(), team.getName(), memberVOList);
            teamVOList.add(vo);
        }
        return teamVOList;
    }

    public Optional<Team> findTeamByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName);
    }
}
