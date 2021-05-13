package com.assign.organization.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexExpression {

    public static final String NAME_REGEX = "^[ㄱ-ㅎ|ㅏ-ㅣ가-힣a-zA-Z]{1}\\s[ㄱ-ㅎ|ㅏ-ㅣ가-힣a-zA-Z]{2,3}$";
    public static final String CELL_PHONE_REGEX = "^\\d{3}[-]\\d{4}[-]\\d{4}$";
    public static final String BUSINESS_CALL_REGEX = "^\\d{4}$";
    public static final String POSITION_REGEX = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]*$";
    public static final String DUTY_REGEX = "^팀[장|원]$";

}
