package com.assign.organization.utils;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.exception.InvalidCSVFileException;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CSVReader {

    private static final String SEPARATOR = ",";
    private static final String NAME_SEPARATOR = " ";

    private static final int BUFFER_SIZE = 1024;
    private static final int END_OF_FILE = -1;

    private CSVReader() {
    }

    @ToString
    @AllArgsConstructor
    private static class RawMemberData {
        String memberId;
        String name;
        String enteredDate;
        String businessCall;
        String cellPhone;
        String teamName;
        String position;
        String duty;

        public CSVMemberVO toCSVMemberVO() throws InvalidCSVFileException {
            if (checkDataValid()) {

                Long convertedMemberId = Long.parseLong(memberId);
                LocalDate convertedEnteredDate = LocalDate.parse(enteredDate);

                String[] nameSplit = name.split(NAME_SEPARATOR);
                String lastName = nameSplit[0];
                String firstName = nameSplit[1];

                return new CSVMemberVO(convertedMemberId, convertedEnteredDate,
                        lastName, firstName, teamName, businessCall, cellPhone, duty, position);
            }

            throw new InvalidCSVFileException("raw 데이터를 변환하는데 실패했습니다 : " + this);
        }

        private boolean checkDataValid() {
            return checkMemberIdValid() &&
                    checkNameValid() &&
                    checkEnteredDateValid() &&
                    checkCellPhoneValid() &&
                    checkBusinessCallValid() &&
                    checkPositionValid() &&
                    checkDutyValid() &&
                    !teamName.isEmpty();
        }

        private boolean checkMemberIdValid() {
            try {
                Long.parseLong(memberId);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }

        private boolean checkEnteredDateValid() {
            try {
                LocalDate.parse(enteredDate);
            } catch (DateTimeParseException e) {
                return false;
            }
            return true;
        }

        private boolean checkNameValid() {
            return this.name.matches(RegexExpression.NAME_REGEX) && !this.name.isEmpty();
        }

        private boolean checkCellPhoneValid() {
            return this.cellPhone.matches(RegexExpression.CELL_PHONE_REGEX) && !this.cellPhone.isEmpty();
        }

        private boolean checkBusinessCallValid() {
            return this.businessCall.matches(RegexExpression.BUSINESS_CALL_REGEX) && !this.cellPhone.isEmpty();
        }

        private boolean checkPositionValid() {
            return this.position.matches(RegexExpression.POSITION_REGEX) && !this.position.isEmpty();
        }

        private boolean checkDutyValid() {
            return this.duty.matches(RegexExpression.DUTY_REGEX) && !this.duty.isEmpty();
        }
    }

    public static List<CSVMemberVO> readCSVFile(String csvFilePath) throws InvalidCSVFileException {
        try {
            String dataStr = readDataFromCSVFile(csvFilePath);
            List<String> rawMemberDataList = convertSplitStringData(dataStr);

            return convertRawMemberDataListToCSVMemberVoList(rawMemberDataList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidCSVFileException("파일 경로가 잘못되어 있습니다. : " + e.getMessage());
        }
    }

    private static List<CSVMemberVO> convertRawMemberDataListToCSVMemberVoList(List<String> rawMemberDataList) throws InvalidCSVFileException {

        try {
            List<CSVMemberVO> csvMemberVOList = new ArrayList<>();
            for (String memberData : rawMemberDataList) {
                log.info(memberData);
                CSVMemberVO csvMemberVO = convertRawMemberDataToCSVMemberVO(memberData);
                csvMemberVOList.add(csvMemberVO);
            }


            return csvMemberVOList;
        } catch (InvalidCSVFileException e) {
            e.printStackTrace();
            throw new InvalidCSVFileException(e.getMessage());
        }
    }

    private static CSVMemberVO convertRawMemberDataToCSVMemberVO(String rawMemberData) throws InvalidCSVFileException {

        String[] split = rawMemberData.split(SEPARATOR);
        try {

            String memberId = split[1].trim();
            String name = split[2].trim();
            String endteredDate = split[3].trim();
            String businessCall = split[4].trim();
            String cellPhone = split[5].trim();
            String teamName = split[6].trim();
            String position = split[7].trim();
            String duty = split[8].trim();

            RawMemberData memberData =
                    new RawMemberData(memberId, name, endteredDate, businessCall, cellPhone, teamName, position, duty);

            return memberData.toCSVMemberVO();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidCSVFileException(e.getMessage());
        }
    }

    private static List<String> convertSplitStringData(String fileData) {
        String[] splitData = fileData.split("\\n");
        return Arrays.asList(splitData);
    }

    private static String readDataFromCSVFile(String filePath) throws IOException {

        FileChannel fileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.SYNC);
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        int read = fileChannel.read(byteBuffer);

        StringBuilder stringBuilder = new StringBuilder();

        while (read != END_OF_FILE) {

            byteBuffer.flip();

            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);

            while (charBuffer.hasRemaining()) {
                stringBuilder.append(charBuffer.get());
            }

            byteBuffer.clear();
            read = fileChannel.read(byteBuffer);
        }
        fileChannel.close();

        return stringBuilder.toString();
    }
}
