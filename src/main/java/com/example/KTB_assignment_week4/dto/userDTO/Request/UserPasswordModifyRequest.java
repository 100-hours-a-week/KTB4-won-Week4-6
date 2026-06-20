package com.example.KTB_assignment_week4.dto.userDTO.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordModifyRequest {
    //비밀번호 검증은 Service 계층에서 PasswordValidator를 사용해 검증합니다.
    String password;
}
