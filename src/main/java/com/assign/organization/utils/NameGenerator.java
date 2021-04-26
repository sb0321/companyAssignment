package com.assign.organization.utils;

public class NameGenerator {

    public static String generate(String name, int duplicated) {

        if(duplicated == 0) {
            return name;
        }

        int left = duplicated % 24;
        int loop = duplicated / 24;

        if(loop != 0) {
            name += (char)('A' + loop);
        }

        return name + (char)('A' + left - 1);
    }
}
