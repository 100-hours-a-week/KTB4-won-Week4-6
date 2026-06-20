package com.example.KTB_assignment_week4.dto.userDTO.Response;

import com.example.KTB_assignment_week4.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSignupResponse {
    private final Long userId;
    private final String email;
    private final String nickname;

    public static UserSignupResponse from(User user){
        return new UserSignupResponse(user.getUserId(), user.getEmail(), user.getNickname());
    }
}
