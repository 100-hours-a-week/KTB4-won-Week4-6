package com.example.KTB_assignment_week4.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BusinessException{

    public ConflictException(String code) {
        super(code, HttpStatus.CONFLICT);   //409
    }
}
