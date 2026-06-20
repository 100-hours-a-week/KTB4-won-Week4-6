package com.example.KTB_assignment_week4.controller;

import com.example.KTB_assignment_week4.dto.userDTO.Request.UserInfoModifyRequest;
import com.example.KTB_assignment_week4.dto.userDTO.Request.UserLoginRequest;
import com.example.KTB_assignment_week4.dto.userDTO.Request.UserPasswordModifyRequest;
import com.example.KTB_assignment_week4.dto.userDTO.Request.UserSignupRequest;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserInfoResponse;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserLoginResponse;
import com.example.KTB_assignment_week4.response.ApiResponse;
import com.example.KTB_assignment_week4.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    public final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")  //로그인 메소드
    public ResponseEntity<ApiResponse<UserLoginResponse>> userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest){
        UserLoginResponse response = userService.login(userLoginRequest);
        return ResponseEntity.ok(
                ApiResponse.of("USER_LOGIN", response)
                );
    }

    @PostMapping("/signup") //회원가입 메소드
    public Long userSignup(@Valid @RequestBody UserSignupRequest userSignupRequest){
        Long response = userService.signup(userSignupRequest);
        return response;
    }

    @GetMapping("/{userId}")    //사용자 정보 수정 페이지 진입 메소드
    public UserInfoResponse getUserInfo(@PathVariable Long userId){
        UserInfoResponse userInfoResponse = userService.getUserInfo(userId);

        return userInfoResponse;
    }

    @PutMapping("/{userId}")    //사용자 정보 수정 메소드
    public String updateUserInfo(@PathVariable Long userId, @Valid @RequestBody UserInfoModifyRequest userInfoModifyRequest){
        String response = userService.modifyUserInfo(userId, userInfoModifyRequest);

        return response;
    }

    @PutMapping("/{userId}/password")   //사용자 비밀번호 수정 메소드
    public String updateUserPassword(@PathVariable Long userId, @Valid @RequestBody UserPasswordModifyRequest userPasswordModifyRequest){
        String response = userService.modifyUserPassword(userId, userPasswordModifyRequest);

        return response;
    }

    @PatchMapping("/{userId}") //사용자 삭제 메소드
    public String deleteUser(@PathVariable Long userId){
        String response = userService.deleteUser(userId);
        return response;
    }
}
