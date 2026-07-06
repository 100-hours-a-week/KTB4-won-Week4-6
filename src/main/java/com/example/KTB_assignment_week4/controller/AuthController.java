package com.example.KTB_assignment_week4.controller;

import com.example.KTB_assignment_week4.configuration.jwt.TokenResult;
import com.example.KTB_assignment_week4.dto.authDTO.request.LoginRequest;
import com.example.KTB_assignment_week4.dto.authDTO.response.LoginResponse;
import com.example.KTB_assignment_week4.dto.authDTO.response.SignupResponse;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserSignupRequest;
import com.example.KTB_assignment_week4.response.ApiResponse;
import com.example.KTB_assignment_week4.service.AuthService;
import com.example.KTB_assignment_week4.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(CsrfToken csrfToken){ //프론트엔드로 CSRF 토큰 전달
        return csrfToken;
    }

    @PostMapping("/signup") //회원가입 메소드
    public ResponseEntity<ApiResponse<SignupResponse>> userSignup(@Valid @RequestBody UserSignupRequest userSignupRequest){
        SignupResponse response = authService.signup(userSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("USER_SIGNUP", response));
    }

    @PostMapping("/login")  //로그인 메소드
    public ResponseEntity<ApiResponse<LoginResponse>> userLogin( @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response){
        TokenResult tokenResult = authService.login(loginRequest);

        ResponseCookie refreshTokenCookie = ResponseCookie.from(
                "refreshToken",
                tokenResult.refreshToken()
        )
                .httpOnly(true)
                .secure(false)  //https 사용하지 않음
                .sameSite("Lax")    //토큰 설정
                .path("/auth")
                .maxAge(Duration.ofDays(30))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshTokenCookie.getValue()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("USER_LOGIN", new LoginResponse(
                        tokenResult.accessToken()
                                )
                        )
                );
    }
}
