package com.example.KTB_assignment_week4.dto.authDTO.request;

import com.example.KTB_assignment_week4.exception.errorMessage.UserErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest { //로그인 요청 시 사용
    @NotBlank(message = UserErrorMessage.EMAIL_REQUIRED)
    @Email(message = UserErrorMessage.EMAIL_FORM_INCORRECT)
    private String email;
    private String password;
}
