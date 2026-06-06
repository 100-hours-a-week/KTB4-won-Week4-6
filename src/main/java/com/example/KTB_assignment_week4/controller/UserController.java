package com.example.KTB_assignment_week4.controller;

import com.example.KTB_assignment_week4.dto.UserLoginRequest;
import com.example.KTB_assignment_week4.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
