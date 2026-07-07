package com.example.KTB_assignment_week4.configuration.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CustomUserPrincipal{
    private final Long userId;
    private final String email;
    private final String nickname;
    private final String authorities;
    private final String profileImage;
}
