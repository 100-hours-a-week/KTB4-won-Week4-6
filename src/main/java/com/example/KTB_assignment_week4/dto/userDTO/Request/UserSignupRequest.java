package com.example.KTB_assignment_week4.dto.userDTO.request;

import com.example.KTB_assignment_week4.exception.userErrorMessage.UserErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupRequest {
    @NotBlank(message = UserErrorMessage.EMAIL_REQUIRED)
    @Email(message = UserErrorMessage.EMAIL_FORM_INCORRECT)
    private String email;
    private String password;
    @NotBlank(message = UserErrorMessage.NICKNAME_REQUIRED)
    @Size(min = 2, max = 10, message = UserErrorMessage.NICKNAME_LENGTH_LIMIT)
    private String nickname;
    private String profileImage;


}
