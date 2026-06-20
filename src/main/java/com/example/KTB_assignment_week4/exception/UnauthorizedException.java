package com.example.KTB_assignment_week4.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BusinessException{
    public UnauthorizedException(String code) {
        super(code, HttpStatus.UNAUTHORIZED); //401
    }
}
