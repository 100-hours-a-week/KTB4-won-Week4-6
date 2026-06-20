package com.example.KTB_assignment_week4.exception.userErrorMessage;

public class UserErrorMessage {
    //닉네임 에러메세지
    public static final String NICKNAME_REQUIRED = "닉네임은 필수 입력값입니다.";
    public static final String NICKNAME_LENGTH_LIMIT = "닉네임은 2글자 이상 10글자 이하여야합니다.";
    public static final String NICKNAME_ALREADY_EXISTS = "이미 존재하는 닉네임입니다.";

    //이메일 에러메세지
    public static final String EMAIL_REQUIRED = "이메일은 필수 입력값입니다.";
    public static final String EMAIL_FORM_INCORRECT = "이메일 형식이 올바르지 않습니다.";
    public static final String EMAIL_ALREADY_EXISTS = "이미 존재하는 이메일입니다.";

    //비밀번호 에러메세지
    public static final String PASSWORD_LENGTH_LIMIT = "비밀번호는 8자 이상 20자 이하여야 합니다.";
    public static final String PASSWORD_CANNOT_CONTAINS_BLANK = "비밀번호는 공백을 포함할 수 없습니다.";
    public static final String PASSWORD_MUST_CONTAIN_UPPERCASE = "비밀번호에는 대문자가 최소 1개 포함되어야 합니다.";
    public static final String PASSWORD_MUST_CONTAIN_LOWERCASE = "비밀번호에는 소문자가 최소 1개 포함되어야 합니다.";
    public static final String PASSWORD_MUST_CONTAIN_NUMBER = "비밀번호에는 숫자를 최소 1개 포함되어야 합니다.";
    public static final String PASSWORD_MUST_CONTAIN_SPECIAL_LETTER = "비밀번호에는 특수문자가 최소 1개 포함되어야 합니다.";

    //로그인 에러메세지
    public static final String USER_UNAUTHORIZED = "로그인이 필요한 기능입니다";
    public static final String EMAIL_AND_PASSWORD_INCORRECT = "이메일과 비밀번호가 일치하지 않습니다.";

    //사용자 조회 에러메세지
    public static final String USER_NOT_FOUND = "사용자가 존재하지 않습니다.";
}
