package com.assign.organization.controller.main;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.service.member.MemberService;
import com.assign.organization.utils.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainAPIController {

    @Value(value = "${csv.data}")
    private String csvFilePath;

    private final MemberService memberService;

    @GetMapping("/init")
    public void init() throws IOException {
        List<CSVMemberVO> csvMemberVOList = CSVReader.readCSVFile(csvFilePath);
        memberService.insertMembersFromCSVMemberVOList(csvMemberVOList);
    }

}
