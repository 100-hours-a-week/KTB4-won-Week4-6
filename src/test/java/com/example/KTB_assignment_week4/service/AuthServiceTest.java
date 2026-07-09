package com.example.KTB_assignment_week4.service;

import com.example.KTB_assignment_week4.configuration.jwt.CustomUserPrincipal;
import com.example.KTB_assignment_week4.configuration.jwt.JwtTokenProvider;
import com.example.KTB_assignment_week4.configuration.jwt.TokenResult;
import com.example.KTB_assignment_week4.domain.user.User;
import com.example.KTB_assignment_week4.domain.user.UserRole;
import com.example.KTB_assignment_week4.dto.authDTO.request.LoginRequest;
import com.example.KTB_assignment_week4.dto.authDTO.request.SignupRequest;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserDeleteRequest;
import com.example.KTB_assignment_week4.exception.ConflictException;
import com.example.KTB_assignment_week4.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UserRepository userRepository;
    @Mock
    JwtTokenProvider jwtTokenProvider;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthService authService;

    @Test
    void loginSuccessTest() {
        //given
        User testUser = new User("사과", "apple@naver.com", "Ilikeapple12!", UserRole.USER, "이미지");

        LoginRequest request = new LoginRequest(testUser.getEmail(), testUser.getPassword());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(testUser.getEmail(), testUser.getPassword());
        Authentication authentication = mock(Authentication.class);

        //when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByEmailAndIsDeletedFalse(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(jwtTokenProvider.createAccessToken(any(), anyString(), anyString())).thenReturn("accessToken");
        when(jwtTokenProvider.createRefreshToken(any())).thenReturn("refreshToken");
        TokenResult tokenResult = new TokenResult("accessToken", "refreshToken");
        //then
        assertThat(authService.login(request)).isEqualTo(tokenResult);
    }

    @Test
    void emailDuplicationTest(){
        //given
        String email = "apple@naver.com";

        //when
        when(userRepository.existsByEmailAndIsDeletedFalse("apple@naver.com")).thenReturn(true);

        //then
        assertThrows(ConflictException.class, () -> authService.checkEmailDuplication(email));
    }

    @Test
    void nicknameDuplicationTest(){
        //given
        String nickname = "사과사과";

        //when
        when(userRepository.existsByNicknameAndIsDeletedFalse("사과사과")).thenReturn(true);

        //then
        assertThrows(ConflictException.class, () -> authService.checkNicknameDuplication(nickname));
    }

    @Test
    void signupSuccessTest() {
        //given
        SignupRequest signupRequest = new SignupRequest("apple@naver.com", "Ilikeapple12!", "사과사과", "이미지");
        User newUser = new User(signupRequest.getNickname(), signupRequest.getEmail(), signupRequest.getPassword(), UserRole.USER, signupRequest.getProfileImage());
        //when
        when(userRepository.existsByNicknameAndIsDeletedFalse("사과사과")).thenReturn(false);
        when(userRepository.existsByEmailAndIsDeletedFalse("apple@naver.com")).thenReturn(false);

        //then
        assertThat(authService.signup(signupRequest).getEmail()).isEqualTo(newUser.getEmail());
        assertThat(authService.signup(signupRequest).getNickname()).isEqualTo(newUser.getNickname());
    }

    @Test
    void deleteUser() {
        // given
        UserDeleteRequest userDeleteRequest = new UserDeleteRequest("탈퇴 사유");
        CustomUserPrincipal principal = new CustomUserPrincipal(1L, "apple@naver.com", "ROLE_USER");

        User deleteTargetUser = mock(User.class);

        when(userRepository.findById(principal.getUserId()))
                .thenReturn(Optional.of(deleteTargetUser));

        // when
        authService.deleteUser(userDeleteRequest, principal);

        // then
        verify(userRepository, times(1)).findById(principal.getUserId());
        verify(deleteTargetUser, times(1)).deleteUser("탈퇴 사유");
    }

}