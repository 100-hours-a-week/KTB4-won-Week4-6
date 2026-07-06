package com.example.KTB_assignment_week4.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(CsrfToken csrfToken){ //프론트엔드로 CSRF 토큰 전달
        return csrfToken;
    }
}
