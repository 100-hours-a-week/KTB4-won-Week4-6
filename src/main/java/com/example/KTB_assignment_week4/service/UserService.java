package com.example.KTB_assignment_week4.service;

import com.example.KTB_assignment_week4.configuration.jwt.CustomUserPrincipal;
import com.example.KTB_assignment_week4.domain.user.User;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserDeleteRequest;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserInfoModifyRequest;
import com.example.KTB_assignment_week4.dto.userDTO.request.UserPasswordModifyRequest;
import com.example.KTB_assignment_week4.dto.userDTO.response.UserInfoModifyResponse;
import com.example.KTB_assignment_week4.dto.userDTO.response.UserInfoResponse;
import com.example.KTB_assignment_week4.exception.ConflictException;
import com.example.KTB_assignment_week4.exception.NotFoundException;
import com.example.KTB_assignment_week4.exception.userErrorMessage.UserErrorMessage;
import com.example.KTB_assignment_week4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

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

        String modifiedPassword = userPasswordModifyRequest.getPassword();

        Optional<User> optionalPasswordModifyTargetUser = userRepository.findById(userId);
        User passwordModifyTargetUser = optionalPasswordModifyTargetUser.orElseThrow(
                () -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND)
        );
        passwordModifyTargetUser.changePassword(modifiedPassword);  //패스워드 검증은 유저 도메인 내에 존재해 따로 진행하지 않음, 더티체킹으로 update 쿼리 전송
    }

    @Transactional
    public void deleteUser(UserDeleteRequest userDeleteRequest, CustomUserPrincipal principal){

        Long userId = principal.getUserId();

        Optional<User> optionalDeleteTargetUser = userRepository.findById(userId);
        User deleteTargetUser = optionalDeleteTargetUser.orElseThrow(
                () -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND)
        );

        String deleteReason = userDeleteRequest.getDeleteReason();

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //AccessToken, RefreshToken 삭제해야함!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!AccessToken, RefreshToken 삭제해야함
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        deleteTargetUser.deleteUser(deleteReason);
    }
}
