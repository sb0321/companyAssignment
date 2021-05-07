package com.assign.organization.controller.main;

import com.assign.organization.exception.CSVFileInvalidException;
import com.assign.organization.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainAPIController {

    private final MemberService memberService;

    @Value(value = "${csv.data}")
    private String csvFilePath;

    @GetMapping("/init")
    public void init() throws CSVFileInvalidException {
        memberService.insertMembersFromCSVFile(csvFilePath);
    }

}
