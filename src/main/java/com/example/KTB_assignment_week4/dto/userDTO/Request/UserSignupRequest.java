package com.example.KTB_assignment_week4.dto.userDTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignupRequest {
    @NotNull(message = "id값을 입력해주세요")
    Long userId;
    @NotBlank(message = "이메일은 필수 입력값입니다")
    String email;
    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    String password;
    @NotBlank(message = "닉네임은 필수 입력값입니다")
    String nickname;
    String profileImage;
}
