package com.example.KTB_assignment_week4.dto.errorDTO;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String code;
    private final Object data;

    private ErrorResponse(String code, Object data){
        this.code = code;
        this.data = data;
    }

    public static ErrorResponse of(String code){
        return new ErrorResponse(code, null);
    }

    public static ErrorResponse of(String code, Object data){
        return new ErrorResponse(code, data);
    }
}
