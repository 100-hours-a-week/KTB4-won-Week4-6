package com.example.KTB_assignment_week4.dto.errorDTO;

public class ErrorResponseDto {

    private final String code;
    private final Object data;

    private ErrorResponseDto(String code){
        this.code = code;
        this.data = null;
    }

    public static ErrorResponseDto of(String code){
        return new ErrorResponseDto(code);
    }
}
