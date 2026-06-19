package com.example.KTB_assignment_week4.handler;

import com.example.KTB_assignment_week4.dto.errorDTO.ErrorResponseDto;
import com.example.KTB_assignment_week4.exception.BusinessException;
import com.example.KTB_assignment_week4.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(NotFoundException exception){

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponseDto.of(exception.getCode()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusiness(BusinessException exception){

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponseDto.of(exception.getCode()));
    }
}
