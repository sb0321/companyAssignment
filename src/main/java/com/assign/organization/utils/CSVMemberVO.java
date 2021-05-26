package com.assign.organization.utils;

import com.assign.organization.domain.member.Nationality;
import com.assign.organization.exception.CSVRawDataConvertException;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class CSVMemberVO {

    private static final String CSV_SEPARATOR = ",";
    private static final String NAME_SEPARATOR = " ";

    private final Long memberId;
    private final Nationality nationality;
    private final LocalDate enteredDate;
    private final String lastName;
    private final String firstName;
    private final String teamName;
    private final String businessCall;
    private final String cellPhone;
    private final String duty;
    private final String position;

    public CSVMemberVO(Long memberId, Nationality nationality, LocalDate enteredDate, String lastName,
                       String firstName, String teamName, String businessCall, String cellPhone, String duty, String position) {
        this.memberId = memberId;
        this.nationality = nationality;
        this.enteredDate = enteredDate;
        this.lastName = lastName;
        this.firstName = firstName;
        this.teamName = teamName;
        this.businessCall = businessCall;
        this.cellPhone = cellPhone;
        this.duty = duty;
        this.position = position;
    }

    public static CSVMemberVO from(String rawMemberData) {
        try {
            String[] split = rawMemberData.split(CSV_SEPARATOR);

            String memberId = split[1].trim();
            String name = split[2].trim();
            String enteredDate = split[3].trim();
            String businessCall = split[4].trim();
            String cellPhone = split[5].trim();
            String teamName = split[6].trim();
            String position = split[7].trim();
            String duty = split[8].trim();
            Nationality nationality = Enum.valueOf(Nationality.class, split[9].trim());

            String firstName, lastName;
            String[] splitName = name.split(" ");
            if (nationality.equals(Nationality.ENGLISH)) {
                firstName = splitName[0];
                lastName = splitName[1];
            } else {
                lastName = splitName[0];
                firstName = splitName[1];
            }

            LocalDate convertEnteredDate = LocalDate.parse(enteredDate);

            return new CSVMemberVO(Long.parseLong(memberId), nationality, convertEnteredDate, lastName,
                    firstName, teamName, businessCall, cellPhone, duty, position);
        } catch (RuntimeException e) {
            throw new CSVRawDataConvertException("Raw Data 변환에 실패했습니다 : " + rawMemberData);
        }
    }


}
