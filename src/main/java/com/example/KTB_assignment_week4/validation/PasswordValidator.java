package com.example.KTB_assignment_week4.validation;

import com.example.KTB_assignment_week4.exception.BadRequestException;
import com.example.KTB_assignment_week4.exception.errorMessage.AuthErrorMessage;

public final class PasswordValidator {  //GPT 이용하여 비밀번호 검증 로직 구현

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 20;

    private PasswordValidator() {
        // Utility class
    }

    public static void validate(String password) {
        if (password == null) {
            throw new BadRequestException(AuthErrorMessage.PASSWORD_REQUIRED);
        }

        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            throw new BadRequestException(AuthErrorMessage.PASSWORD_LENGTH_LIMIT);
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char ch : password.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                throw new BadRequestException(AuthErrorMessage.PASSWORD_CANNOT_CONTAINS_BLANK);
            }

            if (Character.isUpperCase(ch)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowerCase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else {
                hasSpecialChar = true;
            }
        }

        if (!hasUpperCase) {
            throw new BadRequestException(AuthErrorMessage.PASSWORD_MUST_CONTAIN_UPPERCASE);
        }

        if (!hasLowerCase) {
            throw new BadRequestException(AuthErrorMessage.PASSWORD_MUST_CONTAIN_LOWERCASE);
        }

        if (!hasDigit) {
            throw new BadRequestException(AuthErrorMessage.PASSWORD_MUST_CONTAIN_NUMBER);
        }

        if (!hasSpecialChar) {
            throw new BadRequestException(AuthErrorMessage .PASSWORD_MUST_CONTAIN_SPECIAL_LETTER);
        }
    }

    public static boolean isValid(String password) {
        try {
            validate(password);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}