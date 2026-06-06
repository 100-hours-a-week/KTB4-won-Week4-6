package com.example.KTB_assignment_week4.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPasswordModifyRequest {
    @NotBlank
    String password;
}
