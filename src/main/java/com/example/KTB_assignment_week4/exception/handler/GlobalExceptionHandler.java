package com.example.KTB_assignment_week4.exception.handler;

import com.example.KTB_assignment_week4.dto.errorDTO.ErrorResponse;
import com.example.KTB_assignment_week4.exception.BusinessException;
import com.example.KTB_assignment_week4.exception.errorMessage.CommonErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException exception){

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ErrorResponse.of(exception.getCode()));
    }

    @ExceptionHandler(Exception.class)  //예기치 못한 500에러 핸들링
    public ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest request){

        log.error(
                "처리되지 않은 서버 예외. method = {}, uri = {}", request.getMethod(), request.getRequestURI(),exception
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception){

        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(CommonErrorMessage.INVALID_INPUT);
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(ErrorResponse.of(errorMessage));
    }
}
