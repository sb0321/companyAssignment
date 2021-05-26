package com.assign.organization.utils;

import com.assign.organization.exception.CSVFileFormatException;
import com.assign.organization.exception.InvalidFilePathException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class CSVReader {

    private static final String CSV_EXTENSION = "csv";

    public BufferedReader getBufferedReaderFromCSVFilePath(String csvFilePath) {
        try {
            checkFileExtension(csvFilePath);
            FileChannel channel = FileChannel.open(Paths.get(csvFilePath), StandardOpenOption.READ);
            return new BufferedReader(Channels.newReader(channel, StandardCharsets.UTF_8.name()));
        } catch (IOException e) {
            throw new InvalidFilePathException("파일의 경로가 잘못되었습니다.");
        }
    }

    private static void checkFileExtension(String csvFilePath) {
        int extensionPos = csvFilePath.lastIndexOf(".");
        if (extensionPos == -1) {
            throw new CSVFileFormatException("파일에 확장자가 없거나 디렉터리입니다.");
        }

        String extension = csvFilePath.substring(extensionPos + 1);
        if (!extension.equalsIgnoreCase(CSV_EXTENSION)) {
            throw new CSVFileFormatException("파일의 포멧이 " + CSV_EXTENSION + "이 아닙니다 : " + extension);
        }
    }
}
