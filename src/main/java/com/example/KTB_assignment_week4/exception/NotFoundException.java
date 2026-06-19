package com.example.KTB_assignment_week4.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BusinessException {
    public NotFoundException(String code) {
        super(code, HttpStatus.NOT_FOUND);
    }
}
