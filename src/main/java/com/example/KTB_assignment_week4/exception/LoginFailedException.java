package com.example.KTB_assignment_week4.exception;

import org.springframework.http.HttpStatus;

public class LoginFailedException extends BusinessException{
    public LoginFailedException() {
        super("이메일 또는 비밀번호가 일치하지 않습니다", HttpStatus.UNAUTHORIZED);
    }
}
