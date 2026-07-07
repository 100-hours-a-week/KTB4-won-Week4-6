package com.example.KTB_assignment_week4.controller;

import com.example.KTB_assignment_week4.configuration.jwt.CustomUserPrincipal;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserDeleteRequest;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserInfoModifyRequest;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserPasswordModifyRequest;
import com.example.KTB_assignment_week4.dto.userDTO.response.UserInfoModifyResponse;
import com.example.KTB_assignment_week4.dto.userDTO.response.UserInfoResponse;
import com.example.KTB_assignment_week4.response.ApiResponse;
import com.example.KTB_assignment_week4.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    public final UserService userService;

    @GetMapping("/info")    //사용자 정보 수정 페이지 진입 메소드
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal CustomUserPrincipal principal){
        UserInfoResponse response = userService.getUserInfo(principal);

        return ResponseEntity.ok(
                ApiResponse.of("USER_INFO", response)
        );
    }

    @PutMapping("/info")    //사용자 정보 수정 메소드
    public ResponseEntity<ApiResponse<UserInfoModifyResponse>> updateUserInfo(
            @Valid @RequestBody UserInfoModifyRequest userInfoModifyRequest,
            @AuthenticationPrincipal CustomUserPrincipal principal){
        UserInfoModifyResponse response = userService.modifyUserInfo(userInfoModifyRequest, principal);

        return ResponseEntity.ok(
                ApiResponse.of("USER_UPDATE", response)
        );
    }

    @PutMapping("/password")   //사용자 비밀번호 수정 메소드
    public ResponseEntity<Void> updateUserPassword(
            @Valid @RequestBody UserPasswordModifyRequest userPasswordModifyRequest,
            @AuthenticationPrincipal CustomUserPrincipal principal){
        userService.modifyUserPassword(userPasswordModifyRequest, principal);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/info") //사용자 삭제 메소드
    public ResponseEntity<Void> deleteUser(
            UserDeleteRequest userDeleteRequest,
            @AuthenticationPrincipal CustomUserPrincipal principal){
        userService.deleteUser(userDeleteRequest, principal);

        return ResponseEntity.noContent().build();
    }
}
