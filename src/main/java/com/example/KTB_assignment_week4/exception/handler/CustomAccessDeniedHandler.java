package com.example.KTB_assignment_week4.exception.handler;

import com.example.KTB_assignment_week4.exception.errorMessage.AuthErrorMessage;
import com.example.KTB_assignment_week4.exception.responsewriter.SecurityErrorResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityErrorResponseWriter securityErrorResponseWriter;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        securityErrorResponseWriter.write(
                response,
                HttpStatus.FORBIDDEN,
                AuthErrorMessage.USER_UNAUTHORIZED
        );
    }
}
