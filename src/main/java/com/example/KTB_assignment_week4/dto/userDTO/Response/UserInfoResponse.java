package com.example.KTB_assignment_week4.dto.userDTO.Response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoResponse {
    @NotBlank
    String email;
    @NotBlank
    String nickname;
    String profileImage;
}
