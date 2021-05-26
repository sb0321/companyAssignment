package com.assign.organization.service.team;

import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.TeamVO;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.exception.CSVFileReadException;
import com.assign.organization.exception.InvalidMemberException;
import com.assign.organization.exception.MemberBusinessCallDuplicationException;
import com.assign.organization.exception.NullCSVFilePathException;
import com.assign.organization.service.duplication.DuplicationService;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.utils.CSVMemberVO;
import com.assign.organization.utils.CSVReader;
import com.assign.organization.utils.DuplicateNameGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberService memberService;
    private final DuplicationService duplicationService;

    private final CSVReader csvReader;
    private final DuplicateNameGenerator duplicateNameGenerator;

    @Value(value = "${insert.batch.size}")
    private int BATCH_SIZE;

    @Transactional
    public void insertTeamsFromDataPath(String csvFilePath) {
        if (csvFilePath == null) {
            throw new NullCSVFilePathException();
        }

        BufferedReader csvBufferedReader = csvReader.getBufferedReaderFromCSVFilePath(csvFilePath);
        saveTeamsAndMembers(csvBufferedReader);
    }

    private void saveTeamsAndMembers(BufferedReader csvReader) {
        while (true) {
            List<String> rawDataList = readDataFromReader(csvReader, BATCH_SIZE);

            if (rawDataList.isEmpty()) {
                break;
            }

            List<CSVMemberVO> csvMemberVOList = convertRawDataListToCSVMemberVOList(rawDataList);
            saveFromCSVMemberVOList(csvMemberVOList);
        }
    }

    private void saveFromCSVMemberVOList(List<CSVMemberVO> csvMemberVOList) {

        for (CSVMemberVO csvMemberVO : csvMemberVOList) {
            Team team = findTeamOrMake(csvMemberVO.getTeamName());
            String firstName = makeNewFirstNameIfDuplicated(csvMemberVO.getFirstName());

            

            Member member = Member.builder()
                    .id(csvMemberVO.getMemberId())
                    .duty(csvMemberVO.getDuty())
                    .cellPhone(csvMemberVO.getCellPhone())
                    .businessCall(csvMemberVO.getBusinessCall())
                    .position(csvMemberVO.getPosition())
                    .enteredDate(csvMemberVO.getEnteredDate())
                    .nationality(csvMemberVO.getNationality())
                    .lastName(csvMemberVO.getLastName())
                    .firstName(firstName)
                    .build();

            member.setTeam(team);
        }
    }

    private Team findTeamOrMake(String teamName) {
        Optional<Team> findTeam = teamRepository.findByTeamName(teamName);
        return findTeam.orElseGet(() -> teamRepository.save(new Team(teamName)));
    }

    private String makeNewFirstNameIfDuplicated(String firstName) {
        long count = memberService.countFirstNameContains(firstName);
        return duplicateNameGenerator.generateNameWhenDuplication(firstName, count);
    }

    private List<String> readDataFromReader(BufferedReader reader, int batchSize) {
        List<String> rawDataList = new LinkedList<>();
        try {
            for (int i = 0; i < batchSize; i++) {
                String rawData = reader.readLine();
                if (rawData == null) {
                    break;
                }
                rawDataList.add(rawData);
            }
            return rawDataList;
        } catch (IOException e) {
            return rawDataList;
        }
    }

    private List<CSVMemberVO> convertRawDataListToCSVMemberVOList(List<String> rawDataList) {
        return rawDataList.stream().map(CSVMemberVO::from).collect(Collectors.toList());
    }

    public List<TeamVO> findAllTeamVOList() {
        List<Team> teamList = teamRepository.findAllTeams();
        return convertTeamListToTeamVOList(teamList);
    }

    private List<TeamVO> convertTeamListToTeamVOList(List<Team> teamList) {
        return teamList.stream().map(TeamVO::from).collect(Collectors.toList());
    }
}
