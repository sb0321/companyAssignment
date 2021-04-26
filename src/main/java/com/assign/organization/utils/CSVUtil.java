package com.assign.organization.utils;

import com.assign.organization.domain.member.CSVMemberDTO;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVUtil {

    public static List<CSVMemberDTO> getCSVData(String path) {

        List<CSVMemberDTO> csvData = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(path))) {

            String[] line;

            while ((line = reader.readNext()) != null) {

                Long id = Long.parseLong(line[0]);
                String name = line[1];
                String businessCall = line[2];
                String cellPhone = line[3];
                String teamName = line[4];
                String position = line[5];
                String duty = line[6];

                CSVMemberDTO dto = CSVMemberDTO
                        .builder()
                        .id(id)
                        .name(name)
                        .position(position)
                        .teamName(teamName)
                        .businessCall(businessCall)
                        .cellPhone(cellPhone)
                        .duty(duty)
                        .build();

                csvData.add(dto);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return csvData;
    }
}
