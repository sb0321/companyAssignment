package com.assign.organization.utils;

import com.assign.organization.exception.InvalidCSVFileException;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Getter
@ToString
public class CSVMemberVO {

    private static final String CSV_SEPARATOR = ",";
    private static final String NAME_SEPARATOR = " ";

    private final Long memberId;
    private final LocalDate enteredDate;
    private final String lastName;
    private final String firstName;
    private final String teamName;
    private final String businessCall;
    private final String cellPhone;
    private final String duty;
    private final String position;

    private CSVMemberVO(Long memberId, LocalDate enteredDate, String lastName, String firstName, String teamName, String businessCall, String cellPhone, String duty, String position) {
        this.memberId = memberId;
        this.enteredDate = enteredDate;
        this.lastName = lastName;
        this.firstName = firstName;
        this.teamName = teamName;
        this.businessCall = businessCall;
        this.cellPhone = cellPhone;
        this.duty = duty;
        this.position = position;
    }

    public static CSVMemberVO from(String rawMemberData) throws InvalidCSVFileException {
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

            if (checkDataValid(memberId, name, enteredDate, businessCall, cellPhone, teamName, position, duty)) {
                String[] splitName = name.split(" ");
                String lastName = splitName[0];
                String firstName = splitName[1];
                Long id = Long.parseLong(memberId);
                LocalDate convertedEnteredDate = LocalDate.parse(enteredDate);
                return new CSVMemberVO(id, convertedEnteredDate, lastName, firstName, teamName, businessCall, cellPhone, duty, position);
            }

            throw new InvalidCSVFileException("raw 데이터를 변환하는데 실패했습니다 : " + "사번: " + memberId + ", 이름: " + name);
        } catch (NullPointerException e) {
            throw new InvalidCSVFileException("data가 null 입니다.");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidCSVFileException("data가 형식에 맞지 않습니다.");
        }
    }

    private static boolean checkDataValid(String memberId,
                                          String name,
                                          String enteredDate,
                                          String businessCall,
                                          String cellPhone,
                                          String teamName,
                                          String position,
                                          String duty) {
        return checkMemberIdValid(memberId) &&
                checkNameValid(name) &&
                checkEnteredDateValid(enteredDate) &&
                checkCellPhoneValid(cellPhone) &&
                checkBusinessCallValid(businessCall) &&
                checkPositionValid(position) &&
                checkDutyValid(duty) &&
                !teamName.isEmpty();
    }

    private static boolean checkMemberIdValid(String memberId) {
        try {
            Long.parseLong(memberId);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean checkEnteredDateValid(String enteredDate) {
        try {
            LocalDate.parse(enteredDate);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private static boolean checkNameValid(String name) {
        return name.matches(Regex.NAME_REGEX);
    }

    private static boolean checkCellPhoneValid(String cellPhone) {
        return cellPhone.matches(Regex.CELL_PHONE_REGEX);
    }

    private static boolean checkBusinessCallValid(String businessCall) {
        return businessCall.matches(Regex.BUSINESS_CALL_REGEX);
    }

    private static boolean checkPositionValid(String position) {
        return position.matches(Regex.POSITION_REGEX);
    }

    private static boolean checkDutyValid(String duty) {
        return duty.matches(Regex.DUTY_REGEX);
    }
}