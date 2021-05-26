package com.assign.organization.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Slf4j
class DuplicateNameGeneratorTests {

    DuplicateNameGenerator duplicateNameGenerator = new DuplicateNameGenerator();

    @Test
    public void testGenerateNameWhenDuplication() {

        String[] sameNames = new String[26];
        Arrays.fill(sameNames, "same");

        log.info(Arrays.toString(sameNames));


        String[] changedNames = new String[26];
        for (int i = 0; i < sameNames.length; i++) {
            changedNames[i] = duplicateNameGenerator.generateNameWhenDuplication(sameNames[i], i);
        }

        log.info(Arrays.toString(changedNames));

        String[] expectedResultNames = new String[26];
        Arrays.fill(expectedResultNames, "same");
        for (int i = 0; i < expectedResultNames.length; i++) {
            if(i == 0) {
                continue;
            }
            expectedResultNames[i] = expectedResultNames[i] + (char) ('A' + i);
        }

        assertArrayEquals(expectedResultNames, changedNames);
    }
}