package com.assign.organization.utils;

public class NameGenerator {

    public static String generateNameWhenDuplication(String name, long duplicationCount) {
        if (checkNameDuplicated(duplicationCount)) {
            return name + (char) ('A' + duplicationCount);
        }
        return name;
    }

    private static boolean checkNameDuplicated(long duplicationCount) {
        return duplicationCount != 0;
    }
}
