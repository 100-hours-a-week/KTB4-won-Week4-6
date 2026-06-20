package com.example.KTB_assignment_week4.dto.userDTO.Request;

import com.example.KTB_assignment_week4.exception.userErrorMessage.UserErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupRequest {
    @NotNull(message = "id값을 입력해주세요")
    Long userId;
    @NotBlank(message = UserErrorMessage.EMAIL_REQUIRED)
    @Email(message = UserErrorMessage.EMAIL_FORM_INCORRECT)
    String email;
    //비밀번호 검증은 Service 계층에서 PasswordValidator를 사용해 검증합니다.
    String password;
    @NotBlank(message = UserErrorMessage.NICKNAME_REQUIRED)
    @Size(min = 2, max = 10, message = UserErrorMessage.NICKNAME_LENGTH_LIMIT)
    String nickname;
    String profileImage;
}
