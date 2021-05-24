package com.assign.organization.utils;

import com.assign.organization.exception.InvalidCSVFileException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class CSVReader {

    private static final String CSV_EXTENSION = "csv";

    private static BufferedReader bufferedReader;

    private CSVReader() {
    }

    public static void setCSVFile(String csvFilePath) throws InvalidCSVFileException {
        try {
            if (!checkFileExtension(csvFilePath)) {
                throw new InvalidCSVFileException("파일 확장자가 " + CSV_EXTENSION + "이 아닙니다.");
            }

            FileChannel fileChannel = FileChannel.open(Paths.get(csvFilePath), StandardOpenOption.READ);
            bufferedReader = new BufferedReader(Channels.newReader(fileChannel, StandardCharsets.UTF_8.name()));

        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidCSVFileException("파일 경로가 잘못되어 있습니다. : " + e.getMessage());
        } catch (InvalidCSVFileException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static List<CSVMemberVO> readCSVMemberVOList(int capacity) throws InvalidCSVFileException {
        List<CSVMemberVO> csvMemberVOList = new LinkedList<>();
        try {
            for (int i = 0; i < capacity; i++) {
                String rawMemberData = bufferedReader.readLine();

                if (rawMemberData == null) {
                    return csvMemberVOList;
                }
                csvMemberVOList.add(CSVMemberVO.from(rawMemberData));
            }
        } catch (NullPointerException | IOException e) {
            throw new InvalidCSVFileException("CSVReader를 초기화 하지 않았습니다");
        }
        return csvMemberVOList;
    }

    public static void close() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
                bufferedReader = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            bufferedReader = null;
        }
    }

    private static boolean checkFileExtension(String csvFilePath) {
        int extensionPos = csvFilePath.lastIndexOf(".");
        if (extensionPos == -1) {
            return false;
        }

        String extension = csvFilePath.substring(extensionPos + 1);
        return extension.equalsIgnoreCase(CSV_EXTENSION);
    }
}
