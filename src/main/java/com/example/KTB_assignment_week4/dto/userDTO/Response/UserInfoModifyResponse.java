package com.example.KTB_assignment_week4.dto.userDTO.Response;

import com.example.KTB_assignment_week4.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserInfoModifyResponse {
    private final String nickname;
    private final String profileImage;

    public static UserInfoModifyResponse from(User user){
        return new UserInfoModifyResponse(user.getNickname(), user.getProfileImage());
    }
}
