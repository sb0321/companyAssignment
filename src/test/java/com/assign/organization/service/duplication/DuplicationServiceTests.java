package com.assign.organization.service.duplication;

import com.assign.organization.domain.duplication.Duplication;
import com.assign.organization.domain.duplication.repository.DuplicationRepository;
import com.assign.organization.exception.NullDuplicationNameException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class DuplicationServiceTests {

    @Autowired
    DuplicationService duplicationService;

    @Autowired
    DuplicationRepository duplicationRepository;

    @BeforeAll
    void init() {
        Duplication duplication = new Duplication("test");
        duplicationRepository.save(duplication);
    }

    @Test
    void testIncreaseDuplicationCountIfDuplicated() {
        assertThrows(NullDuplicationNameException.class,
                () -> duplicationService.makeDuplicationAndIncreaseCount(null));

        assertDoesNotThrow(() -> {
            String duplication = "test";
            duplicationService.makeDuplicationAndIncreaseCount(duplication);
            assertEquals(1L, duplicationRepository.findById(duplication).get().getDuplicationCount());
        });

        assertDoesNotThrow(() -> {
            String firstDuplication = "first";
            duplicationService.makeDuplicationAndIncreaseCount(firstDuplication);
            assertEquals(0L, duplicationRepository.findById(firstDuplication).get().getDuplicationCount());
        });
    }
}