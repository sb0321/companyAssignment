package com.assign.organization.domain.member;

import com.assign.organization.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTests {

    private static final String LEADER_DUTY = "팀장";
    private static final String FOLLOWER_DUTY = "팀원";

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void init() {
    }

}