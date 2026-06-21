package com.example.KTB_assignment_week4.controller;

import com.example.KTB_assignment_week4.dto.userDTO.Request.*;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserInfoModifyResponse;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserInfoResponse;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserLoginResponse;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserSignupResponse;
import com.example.KTB_assignment_week4.response.ApiResponse;
import com.example.KTB_assignment_week4.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    public final UserService userService;

    @PostMapping("/login")  //로그인 메소드
    public ResponseEntity<ApiResponse<UserLoginResponse>> userLogin(
            @Valid @RequestBody UserLoginRequest userLoginRequest,
            HttpSession session){
        UserLoginResponse response = userService.login(userLoginRequest, session);

        return ResponseEntity.ok(
                ApiResponse.of("USER_LOGIN", response)
                );
    }

    @PostMapping("/signup") //회원가입 메소드
    public ResponseEntity<ApiResponse<UserSignupResponse>> userSignup(@Valid @RequestBody UserSignupRequest userSignupRequest){
        UserSignupResponse response = userService.signup(userSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of("USER_SIGNUP", response));
    }

    @GetMapping("/info")    //사용자 정보 수정 페이지 진입 메소드
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(HttpSession session){
        UserInfoResponse response = userService.getUserInfo(session);

        return ResponseEntity.ok(
                ApiResponse.of("USER_INFO", response)
        );
    }

    @PutMapping("/info")    //사용자 정보 수정 메소드
    public ResponseEntity<ApiResponse<UserInfoModifyResponse>> updateUserInfo(
            @Valid @RequestBody UserInfoModifyRequest userInfoModifyRequest,
            HttpSession session){
        UserInfoModifyResponse response = userService.modifyUserInfo(userInfoModifyRequest, session);

        return ResponseEntity.ok(
                ApiResponse.of("USER_UPDATE", response)
        );
    }

    @PutMapping("/password")   //사용자 비밀번호 수정 메소드
    public ResponseEntity<Void> updateUserPassword(
            @Valid @RequestBody UserPasswordModifyRequest userPasswordModifyRequest,
            HttpSession session){
        userService.modifyUserPassword(userPasswordModifyRequest, session);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/info") //사용자 삭제 메소드
    public ResponseEntity<Void> deleteUser(
            UserDeleteRequest userDeleteRequest,
            HttpSession session){
        userService.deleteUser(userDeleteRequest, session);

        return ResponseEntity.noContent().build();
    }
}
