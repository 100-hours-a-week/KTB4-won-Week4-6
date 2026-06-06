package com.example.KTB_assignment_week4.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequest { //로그인 요청 시 사용
    @NotNull
    private String email;
    @NotNull
    private String password;
}
