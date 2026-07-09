package com.example.KTB_assignment_week4.exception.handler;

import com.example.KTB_assignment_week4.exception.errorMessage.AuthErrorMessage;
import com.example.KTB_assignment_week4.exception.responsewriter.SecurityErrorResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SecurityErrorResponseWriter securityErrorResponseWriter;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        securityErrorResponseWriter.write(
                response,
                HttpStatus.UNAUTHORIZED,
                AuthErrorMessage.AUTHENTICATION_FAILED
        );
    }
}
