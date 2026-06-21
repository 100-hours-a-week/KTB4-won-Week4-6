package com.example.KTB_assignment_week4.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BusinessException{
    public BadRequestException(String code) {
        super(code, HttpStatus.BAD_REQUEST); //400
    }
}
