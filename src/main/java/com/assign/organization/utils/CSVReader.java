package com.assign.organization.utils;

import com.assign.organization.domain.member.CSVMemberVO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVReader {

    private static final String SEPARATOR = ",";
    private static final String FOLLOWER = "팀원";
    private static final int DUTY_CONTAIN_ARRAY_LENGTH = 7;

    public static List<CSVMemberVO> readCSVFile(String csvFilePath) throws IOException {
        BufferedReader csvFileBufferedReader = getFileBufferedReader(csvFilePath);
        List<CSVMemberVO> csvMemberVOListFromReader = getCSVMemberVOListFromReader(csvFileBufferedReader);
        csvFileBufferedReader.close();
        return csvMemberVOListFromReader;
    }

    private static BufferedReader getFileBufferedReader(String filePath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filePath));
    }

    private static List<CSVMemberVO> getCSVMemberVOListFromReader(BufferedReader bufferedReader) throws IOException {
        List<CSVMemberVO> csvMemberVOList = new ArrayList<>();

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            CSVMemberVO csvMemberVO = convertStringInfoToCSVMemberVO(line);
            csvMemberVOList.add(csvMemberVO);
        }

        return csvMemberVOList;
    }

    private static CSVMemberVO convertStringInfoToCSVMemberVO(String info) {
        String[] splitInfo = info.split(SEPARATOR);

        Long id = Long.parseLong(splitInfo[0]);
        String name = splitInfo[1];
        String businessCall = splitInfo[2];
        String cellPhone = splitInfo[3];
        String teamName = splitInfo[4];
        String position = splitInfo[5];
        String duty = getDuty(splitInfo);

        return new CSVMemberVO(id, name, teamName, businessCall, cellPhone, duty, position);
    }

    private static String getDuty(String[] info) {
        if(checkDutyContains(info)) {
            return info[6];
        }
        return FOLLOWER;
    }

    private static boolean checkDutyContains(String[] info) {
        return info.length == DUTY_CONTAIN_ARRAY_LENGTH;
    }

}
