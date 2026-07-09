package com.example.KTB_assignment_week4.service;

import com.example.KTB_assignment_week4.configuration.jwt.CustomUserPrincipal;
import com.example.KTB_assignment_week4.domain.user.User;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserInfoModifyRequest;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserPasswordModifyRequest;
import com.example.KTB_assignment_week4.dto.userDTO.response.UserInfoModifyResponse;
import com.example.KTB_assignment_week4.dto.userDTO.response.UserInfoResponse;
import com.example.KTB_assignment_week4.exception.BadRequestException;
import com.example.KTB_assignment_week4.exception.ConflictException;
import com.example.KTB_assignment_week4.exception.NotFoundException;
import com.example.KTB_assignment_week4.exception.errorMessage.AuthErrorMessage;
import com.example.KTB_assignment_week4.exception.errorMessage.UserErrorMessage;
import com.example.KTB_assignment_week4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoResponse getUserInfo(CustomUserPrincipal principal){
        Long userId = principal.getUserId();

        Optional<User> optionalUserFoundById = userRepository.findById(userId);
        User userFoundById = optionalUserFoundById.orElseThrow(
                () -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND)
        );

        return UserInfoResponse.from(userFoundById);
    }

    @Transactional
    public UserInfoModifyResponse modifyUserInfo(UserInfoModifyRequest userInfoModifyRequest, CustomUserPrincipal principal){

        Long userId = principal.getUserId();

        String newNickname = userInfoModifyRequest.getNickname();
        String newProfileImage = userInfoModifyRequest.getProfileImage();

        if(userRepository.existsByNicknameAndIsDeletedFalse(newNickname)){   //닉네임 중복 체크
            throw new ConflictException(UserErrorMessage.NICKNAME_ALREADY_EXISTS);
        }

        Optional<User> optionalModifyTargetUser = userRepository.findById(userId);
        User modifyTargetUser = optionalModifyTargetUser.orElseThrow(
                () -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND)
        );

        modifyTargetUser.changeNickname(newNickname);
        modifyTargetUser.changeProfileImage(newProfileImage);   //더티체킹으로 update쿼리 자동 전송

        return UserInfoModifyResponse.from(modifyTargetUser);
    }

    @Transactional
    public void modifyUserPassword(UserPasswordModifyRequest userPasswordModifyRequest, CustomUserPrincipal principal){

        Long userId = principal.getUserId();

        User userFindById = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND));

        String originalPassword = userPasswordModifyRequest.getOriginalPassword();
        String changePassword = userPasswordModifyRequest.getChangedPassword();

        String encodedOriginalPassword = passwordEncoder.encode(originalPassword);
        String encodedChangePassword = passwordEncoder.encode(changePassword);

        if(!encodedOriginalPassword.equals(userFindById.getPassword())){
            throw new BadRequestException(AuthErrorMessage.PASSWORD_INCORRECT);     //비밀번호 변경 시 비밀번호 일치 여부 확인 로직 추가
        }

        if(encodedChangePassword.equals(userFindById.getPassword())){
            throw new BadRequestException(AuthErrorMessage.PASSWORD_ALREADY_USED);  //현재 비밀번호로 변경 시도 시 에러 발생
        }

        userFindById.changePassword(changePassword);  //패스워드 검증은 유저 도메인 내에 존재해 따로 진행하지 않음, 더티체킹으로 update 쿼리 전송
    }
}
