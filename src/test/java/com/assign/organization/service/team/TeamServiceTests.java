package com.assign.organization.service.team;

import com.assign.organization.domain.member.CSVMemberVO;
import com.assign.organization.domain.member.Contact;
import com.assign.organization.domain.member.Member;
import com.assign.organization.domain.team.Team;
import com.assign.organization.domain.team.repository.TeamRepository;
import com.assign.organization.domain.team.TeamVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeamServiceTests {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;


}