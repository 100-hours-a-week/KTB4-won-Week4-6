package com.example.KTB_assignment_week4.dto.authDTO.response;

import com.example.KTB_assignment_week4.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignupResponse {
    private final Long id;
    private final String email;
    private final String nickname;

    public static SignupResponse from(User user){
        return new SignupResponse(user.getId(), user.getEmail(), user.getNickname());
    }
}
