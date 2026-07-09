package com.example.KTB_assignment_week4.service;

import com.example.KTB_assignment_week4.configuration.jwt.CustomUserPrincipal;
import com.example.KTB_assignment_week4.configuration.jwt.JwtTokenProvider;
import com.example.KTB_assignment_week4.configuration.jwt.TokenResult;
import com.example.KTB_assignment_week4.domain.user.User;
import com.example.KTB_assignment_week4.domain.user.UserRole;
import com.example.KTB_assignment_week4.dto.authDTO.request.LoginRequest;
import com.example.KTB_assignment_week4.dto.authDTO.request.SignupRequest;
import com.example.KTB_assignment_week4.dto.authDTO.response.SignupResponse;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserDeleteRequest;
import com.example.KTB_assignment_week4.exception.ConflictException;
import com.example.KTB_assignment_week4.exception.NotFoundException;
import com.example.KTB_assignment_week4.exception.UnauthorizedException;
import com.example.KTB_assignment_week4.exception.errorMessage.AuthErrorMessage;
import com.example.KTB_assignment_week4.exception.errorMessage.UserErrorMessage;
import com.example.KTB_assignment_week4.repository.UserRepository;
import com.example.KTB_assignment_week4.validation.PasswordValidator;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResult login(@Valid LoginRequest loginRequest){

        PasswordValidator.validate(loginRequest.getPassword()); //비밀번호 형식 검증

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        authenticationManager.authenticate(authenticationToken);

        User userFindByEmail = userRepository.findByEmailAndIsDeletedFalse(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException(UserErrorMessage.USER_NOT_FOUND));

        String authority = "ROLE_" + userFindByEmail.getUserRole().name();  //ROLE_ 형태로 맞추기 위해 문자열 추가

        String accessToken = jwtTokenProvider.createAccessToken(
                userFindByEmail.getId(),
                userFindByEmail.getEmail(),
                authority);
        String refreshToken = jwtTokenProvider.createRefreshToken(userFindByEmail.getId());             //AccessToken과 RefreshToken 생성

        return new TokenResult(accessToken, refreshToken);
    }
    @Transactional
    public SignupResponse signup(@Valid SignupRequest signupRequest){
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        String nickname = signupRequest.getNickname();
        String profileImage = signupRequest.getProfileImage();

        checkEmailDuplication(email);
        checkNicknameDuplication(nickname);  //이메일과 닉네임 중복 체크 후 중복된다면 예외 발생
        PasswordValidator.validate(password);

        password = passwordEncoder.encode(password);    //Bean에 등록된 암호화 모듈 사용하여 암호화된 값을 저장

        User user = new User(nickname, email, password, UserRole.USER, profileImage);

        userRepository.save(user);

        return SignupResponse.from(user);
    }

    public String refreshAccessToken(String refreshToken){      //refreshToken을 이용한 accessToken 재발급
        if(refreshToken == null){
            throw new UnauthorizedException(AuthErrorMessage.USER_UNAUTHENTICATED);
        }

        Claims claims = jwtTokenProvider.validateRefreshToken(refreshToken);

        Long userId = jwtTokenProvider.getUserId(claims);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND));

        if(user.getIsDeleted()){
            throw new UnauthorizedException(AuthErrorMessage.USER_DELETED);
        }

        String authority = "ROLE_" + user.getUserRole();

        return jwtTokenProvider.createAccessToken(
                userId,
                user.getEmail(),
                authority);
    }

    @Transactional
    public void deleteUser(UserDeleteRequest userDeleteRequest, CustomUserPrincipal principal){

        Long userId = principal.getUserId();

        User deleteTargetUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND)
        );

        String deleteReason = userDeleteRequest.getDeleteReason();

        deleteTargetUser.deleteUser(deleteReason);
    }

    public void checkEmailDuplication(String email){        //이메일 중복체크 => 중복 시 예외처리
        if(userRepository.existsByEmailAndIsDeletedFalse(email)){
            throw new ConflictException(UserErrorMessage.EMAIL_ALREADY_EXISTS);
        };
    }

    public void checkNicknameDuplication(String nickname){  //닉네임 중복체크 => 중복 시 예외처리
        if(userRepository.existsByNicknameAndIsDeletedFalse(nickname)){
            throw new ConflictException(UserErrorMessage.NICKNAME_ALREADY_EXISTS);
        }
    }
}
