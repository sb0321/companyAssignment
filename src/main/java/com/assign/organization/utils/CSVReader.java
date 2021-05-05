package com.assign.organization.utils;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.exception.CSVFileNotValidException;
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

    private static final int BUFFER_SIZE = 1024;
    private static final int END_OF_FILE = -1;

    private CSVReader() {
    }

    private static class RawMemberData {

        private static final String NAME_REGEX = "^[ㄱ-ㅎ|ㅏ-ㅣ가-힣a-zA-Z0-9]*$";
        private static final String CELL_PHONE_REGEX = "^\\d{3}[-]\\d{4}[-]\\d{4}$";
        private static final String BUSINESS_CALL_REGEX = "^\\d{4}$";
        private static final String POSITION_REGEX = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]*$";
        private static final String DUTY_REGEX = "^팀[장|원]$";

        private final String name;
        private final String teamName;
        private final String businessCall;
        private final String cellPhone;
        private final String duty;
        private final String position;

        public RawMemberData(String rawData) {
            String[] split = rawData.split(",");

            this.name = split[1];
            this.businessCall = split[2];
            this.cellPhone = split[3];
            this.teamName = split[4];
            this.position = split[5];
            this.duty = split[6];
        }

        public CSVMemberVO convertCSVMemberVO() throws CSVFileNotValidException {

            if(!checkDataValid()) {
                throw new CSVFileNotValidException();
            }

            return new CSVMemberVO(name, teamName, businessCall, cellPhone, duty, position);
        }

        private boolean checkDataValid() {
            return checkNameValid() &&
                    checkCellPhoneValid() &&
                    checkBusinessCallValid() &&
                    checkPositionValid() &&
//                    checkDutyValid() &&
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

    public static List<CSVMemberVO> readCSVFile(String csvFilePath) throws CSVFileNotValidException, IOException {

        RandomAccessFile csvFile = new RandomAccessFile(csvFilePath, "r");
        String dataStr = readDataFromRandomAccessFile(csvFile);
        List<String> rawMemberDataList = convertSplitStringData(dataStr);

        return convertRawMemberDataListToCSVMemberVoList(rawMemberDataList);
    }

    private static List<CSVMemberVO> convertRawMemberDataListToCSVMemberVoList(List<String> rawMemberDataList) throws CSVFileNotValidException {

        List<CSVMemberVO> csvMemberVOList = new ArrayList<>();
        for (String memberData : rawMemberDataList) {
            log.info(memberData);
            CSVMemberVO csvMemberVO = convertRawMemberDataToCSVMemberVO(memberData);
            csvMemberVOList.add(csvMemberVO);
        }

        return csvMemberVOList;
    }

    private static CSVMemberVO convertRawMemberDataToCSVMemberVO(String rawMemberData) throws CSVFileNotValidException {
        RawMemberData memberData = new RawMemberData(rawMemberData);
        return memberData.convertCSVMemberVO();
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
