package com.example.KTB_assignment_week4.controller;

import com.example.KTB_assignment_week4.dto.*;
import com.example.KTB_assignment_week4.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    public final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")  //로그인 메소드
    public String userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest){
        String response = userService.login(userLoginRequest);
        return response;
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
    public void updateUserInfo(@PathVariable Long userId, @Valid @RequestBody UserInfoModifyRequest userInfoModifyRequest){
        userService.modifyUserInfo(userId, userInfoModifyRequest);
    }

    @PutMapping("/{userId}/password")   //사용자 비밀번호 수정 메소드
    public void updateUserPassword(@PathVariable Long userId, @Valid @RequestBody UserPasswordModifyRequest userPasswordModifyRequest){
        userService.modifyUserPassword(userId, userPasswordModifyRequest);
    }
}
