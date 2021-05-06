package com.assign.organization.utils;

import lombok.NonNull;

public class NameGenerator {

    public static String generateNameWhenDuplication(@NonNull String name, long duplicationCount) {
        if (checkNameDuplicated(duplicationCount)) {
            return name + (char) ('A' + duplicationCount);
        }
        return name;
    }

    private static boolean checkNameDuplicated(long duplicationCount) {
        return duplicationCount != 0;
    }
}
