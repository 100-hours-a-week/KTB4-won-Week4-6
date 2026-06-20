package com.example.KTB_assignment_week4.dto.userDTO.Response;

import com.example.KTB_assignment_week4.domain.User;
import com.example.KTB_assignment_week4.domain.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLoginResponse {
    private final Long userId;
    private final String nickname;
    private final String email;
    private final UserRole userRole;

    public static UserLoginResponse from(User user){
        return new UserLoginResponse(user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getUserRole());
    }
}
