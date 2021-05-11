package com.assign.organization.utils;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.exception.InvalidCSVFileException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CSVReader {

    private static final String NAME_REGEX = "^[ㄱ-ㅎ|ㅏ-ㅣ가-힣a-zA-Z0-9]*$";
    private static final String CELL_PHONE_REGEX = "^\\d{3}[-]\\d{4}[-]\\d{4}$";
    private static final String BUSINESS_CALL_REGEX = "^\\d{4}$";
    private static final String POSITION_REGEX = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]*$";
    private static final String DUTY_REGEX = "^팀[장|원]$";

    private static final String SEPARATOR = ",";

    private static final int BUFFER_SIZE = 1024;
    private static final int END_OF_FILE = -1;

    private CSVReader() {
    }

    @AllArgsConstructor
    private static class RawMemberData {
        long num;
        String name;
        String businessCall;
        String cellPhone;
        String teamName;
        String position;
        String duty;

        public CSVMemberVO toCSVMemberVO() throws InvalidCSVFileException {
            if (checkDataValid()) {
                return new CSVMemberVO(name, teamName, businessCall, cellPhone, duty, position);
            }

            throw new InvalidCSVFileException("raw 데이터를 변환하는데 실패했습니다.");
        }

        private boolean checkDataValid() {
            return checkNameValid() &&
                    checkCellPhoneValid() &&
                    checkBusinessCallValid() &&
                    checkPositionValid() &&
                    checkDutyValid() &&
                    !teamName.isEmpty();
        }

        private boolean checkNameValid() {
            return this.name.matches(NAME_REGEX) && !this.name.isEmpty();
        }

        private boolean checkCellPhoneValid() {
            return this.cellPhone.matches(CELL_PHONE_REGEX) && !this.cellPhone.isEmpty();
        }

        private boolean checkBusinessCallValid() {
            return this.businessCall.matches(BUSINESS_CALL_REGEX) && !this.cellPhone.isEmpty();
        }

        private boolean checkPositionValid() {
            return this.position.matches(POSITION_REGEX) && !this.position.isEmpty();
        }

        private boolean checkDutyValid() {
            return this.duty.matches(DUTY_REGEX) && !this.duty.isEmpty();
        }
    }

    public static List<CSVMemberVO> readCSVFile(String csvFilePath) throws InvalidCSVFileException {
        try {
            RandomAccessFile csvFile = new RandomAccessFile(csvFilePath, "r");
            String dataStr = readDataFromRandomAccessFile(csvFile);
            List<String> rawMemberDataList = convertSplitStringData(dataStr);

            return convertRawMemberDataListToCSVMemberVoList(rawMemberDataList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidCSVFileException("파일 경로가 잘못되어 있습니다.");
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
            throw new InvalidCSVFileException("CSV 파일이 손상되었거나 잘못되어 있습니다.");
        }
    }

    private static CSVMemberVO convertRawMemberDataToCSVMemberVO(String rawMemberData) throws InvalidCSVFileException {

        String[] split = rawMemberData.split(SEPARATOR);
        try {

            long num = Long.parseLong(split[0]);
            String name = split[1];
            String businessCall = split[2].trim();
            String cellPhone = split[3].trim();
            String teamName = split[4].trim();
            String position = split[5].trim();
            String duty = split[6].trim();

            RawMemberData memberData = new RawMemberData(num, name, businessCall, cellPhone, teamName, position, duty);

            return memberData.toCSVMemberVO();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidCSVFileException("CSV파일이 손상되었거나 잘못 되어 있습니다.");
        }
    }

    private static List<String> convertSplitStringData(String fileData) {
        String[] splitData = fileData.split("\\n");
        return Arrays.asList(splitData);
    }

    private static String readDataFromRandomAccessFile(RandomAccessFile file) throws IOException {

        FileChannel fileChannel = file.getChannel();
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

        return stringBuilder.toString();
    }
}
