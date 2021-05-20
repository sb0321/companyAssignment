package com.assign.organization.utils;

import com.assign.organization.exception.InvalidCSVFileException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CSVReader {

    private static final int BUFFER_SIZE = 100 * 1024 * 1024;
    private static final int END_OF_FILE = -1;
    private static final String CSV_EXTENSION = "csv";

    private CSVReader() {
    }

    public static List<CSVMemberVO> readCSVFile(String csvFilePath) throws InvalidCSVFileException {
        try {
            if (!checkFileExtension(csvFilePath)) {
                throw new InvalidCSVFileException("파일 확장자가 " + CSV_EXTENSION + "이 아닙니다.");
            }
            String dataStr = readDataFromCSVFile(csvFilePath);
            List<String> rawMemberDataList = convertSplitStringData(dataStr);

            return convertRawMemberDataListToCSVMemberVoList(rawMemberDataList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidCSVFileException("파일 경로가 잘못되어 있습니다. : " + e.getMessage());
        } catch (InvalidCSVFileException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static boolean checkFileExtension(String csvFilePath) {
        int extensionPos = csvFilePath.indexOf(".");
        if (extensionPos == -1) {
            return false;
        }

        String extension = csvFilePath.substring(extensionPos + 1);
        log.info(extension);
        return extension.equalsIgnoreCase(CSV_EXTENSION);
    }

    private static List<CSVMemberVO> convertRawMemberDataListToCSVMemberVoList(List<String> rawMemberDataList) throws InvalidCSVFileException {
        List<CSVMemberVO> csvMemberVOList = new ArrayList<>();
        for (String memberData : rawMemberDataList) {
            log.info(memberData);
            CSVMemberVO csvMemberVO = CSVMemberVO.from(memberData);
            csvMemberVOList.add(csvMemberVO);
        }

        return csvMemberVOList;
    }

    private static List<String> convertSplitStringData(String fileData) {
        String[] splitData = fileData.split("\\n");
        return Arrays.asList(splitData);
    }

    private static String readDataFromCSVFile(String filePath) throws IOException {

        FileChannel fileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.SYNC);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

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
