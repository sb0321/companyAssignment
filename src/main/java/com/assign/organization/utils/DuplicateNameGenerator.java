package com.assign.organization.utils;

import lombok.NonNull;

public class DuplicateNameGenerator {

    public String generateNameWhenDuplication(@NonNull String name, long duplicationCount) {
        if (checkNameDuplicated(duplicationCount)) {
            return name + (char) ('A' + duplicationCount);
        }
        return name;
    }

    private boolean checkNameDuplicated(long duplicationCount) {
        return duplicationCount != 0;
    }
}
