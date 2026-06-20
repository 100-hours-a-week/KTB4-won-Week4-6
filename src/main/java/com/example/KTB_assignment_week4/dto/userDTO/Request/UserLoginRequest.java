package com.example.KTB_assignment_week4.dto.userDTO.Request;

import com.example.KTB_assignment_week4.exception.userErrorMessage.UserErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest { //로그인 요청 시 사용
    @NotBlank(message = UserErrorMessage.EMAIL_REQUIRED)
    @Email(message = UserErrorMessage.EMAIL_FORM_INCORRECT)
    private String email;
    //비밀번호 검증은 Service 계층에서 PasswordValidator를 사용해 검증합니다.
    private String password;
}
