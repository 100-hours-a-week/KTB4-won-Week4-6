package com.example.KTB_assignment_week4.handler;

import com.example.KTB_assignment_week4.dto.errorDTO.ErrorResponse;
import com.example.KTB_assignment_week4.exception.BadRequestException;
import com.example.KTB_assignment_week4.exception.BusinessException;
import com.example.KTB_assignment_week4.exception.NotFoundException;
import com.example.KTB_assignment_week4.exception.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException exception){

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponse.of(exception.getCode()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException exception){

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponse.of(exception.getCode()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException exception){

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponse.of(exception.getCode()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleLoginFailed(UnauthorizedException exception) {

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponse.of(exception.getCode()));
    }
}
