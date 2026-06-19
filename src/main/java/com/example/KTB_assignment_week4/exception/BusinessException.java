package com.example.KTB_assignment_week4.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException{

    private final String code;
    private final HttpStatus httpStatus;

    public BusinessException(String code, HttpStatus httpStatus) {
        super(code);    //로그나 기본 예외 출력에서도 코드가 함께 노출되도록 하기 위함
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
