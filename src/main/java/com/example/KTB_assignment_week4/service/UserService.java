package com.example.KTB_assignment_week4.service;

import com.example.KTB_assignment_week4.domain.User;
import com.example.KTB_assignment_week4.domain.UserRole;
import com.example.KTB_assignment_week4.dto.userDTO.Request.*;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserInfoModifyResponse;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserInfoResponse;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserLoginResponse;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserSignupResponse;
import com.example.KTB_assignment_week4.exception.BadRequestException;
import com.example.KTB_assignment_week4.exception.ConflictException;
import com.example.KTB_assignment_week4.exception.NotFoundException;
import com.example.KTB_assignment_week4.exception.UnauthorizedException;
import com.example.KTB_assignment_week4.exception.userErrorMessage.UserErrorMessage;
import com.example.KTB_assignment_week4.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserLoginResponse login(@Valid UserLoginRequest userLoginRequest, HttpSession session){
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        if((password.length() < 8) || (password.length() > 20)){
            throw new BadRequestException(UserErrorMessage.PASSWORD_LENGTH_LIMIT);
        }

        Optional<User> optionalUserFoundByEmail = userRepository.findByEmailAndIsDeletedFalse(email);
        User userFoundByEmail = optionalUserFoundByEmail.orElseThrow(
                () -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND));
        String passwordOfFoundUser = userFoundByEmail.getPassword();
        if(!passwordOfFoundUser.equals(password)){
            throw new UnauthorizedException(UserErrorMessage.EMAIL_AND_PASSWORD_INCORRECT);
        }
        if(userFoundByEmail.getIsDeleted() == true){
            throw new UnauthorizedException(UserErrorMessage.USER_NOT_FOUND);
        }

        session.setAttribute("LOGIN_USER_ID", userFoundByEmail.getUserId());    //현재 로그인한 유저의 ID저장
        session.setAttribute("LOGIN_EXPIRES_AT", LocalDateTime.now().plusMinutes(30).toString()); //해당 로그인 유저의 ID를 30분동안 유효하도록 설정

        return UserLoginResponse.from(userFoundByEmail);
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

    @Transactional
    public UserSignupResponse signup(@Valid UserSignupRequest userSignupRequest){
        String email = userSignupRequest.getEmail();
        String password = userSignupRequest.getPassword();
        String nickname = userSignupRequest.getNickname();
        String profileImage = userSignupRequest.getProfileImage();

        checkEmailDuplication(email);
        checkNicknameDuplication(nickname);  //이메일과 닉네임 중복 체크 후 중복된다면 예외 발생


        User user = new User(nickname, email, password, UserRole.USER, profileImage);

        userRepository.save(user);

        return UserSignupResponse.from(user);
    }

    public Long userAuthorizationCheck(HttpSession session){  //sessionStorage에서 가져온 userId값 검증
        Long userId = (Long) session.getAttribute("LOGIN_USER_ID");
        if(userId == null){     //로그인 정보 없을 시 예외처리
            throw new UnauthorizedException(UserErrorMessage.USER_UNAUTHORIZED);
        }

        String loginExpiresAtAsString = (String) session.getAttribute("LOGIN_EXPIRES_AT");
        LocalDateTime loginExpiresAt = LocalDateTime.parse(loginExpiresAtAsString);

        if(loginExpiresAt.isBefore(LocalDateTime.now())){   //로그인 정보 만료 시 예외처리 및 로그인 정보 삭제
            session.removeAttribute("LOGIN_USER_ID");
            session.removeAttribute("LOGIN_EXPIRES_AT");
            throw new UnauthorizedException(UserErrorMessage.USER_UNAUTHORIZED);
        }

        return userId;
    }

    public UserInfoResponse getUserInfo(HttpSession session){

        Long userId = userAuthorizationCheck(session);

        Optional<User> optionalUserFoundById = userRepository.findById(userId);
        User userFoundById = optionalUserFoundById.orElseThrow(
                () -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND)
        );

        return UserInfoResponse.from(userFoundById);
    }

    @Transactional
    public UserInfoModifyResponse modifyUserInfo(UserInfoModifyRequest userInfoModifyRequest, HttpSession session){

        Long userId = userAuthorizationCheck(session);

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
    public void modifyUserPassword(UserPasswordModifyRequest userPasswordModifyRequest, HttpSession session){

        Long userId = userAuthorizationCheck(session);

        String modifiedPassword = userPasswordModifyRequest.getPassword();

        Optional<User> optionalPasswordModifyTargetUser = userRepository.findById(userId);
        User passwordModifyTargetUser = optionalPasswordModifyTargetUser.orElseThrow(
                () -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND)
        );
        passwordModifyTargetUser.changePassword(modifiedPassword);  //패스워드 검증은 유저 도메인 내에 존재해 따로 진행하지 않음, 더티체킹으로 update 쿼리 전송
    }

    @Transactional
    public void deleteUser(UserDeleteRequest userDeleteRequest, HttpSession session){

        Long userId = userAuthorizationCheck(session);

        Optional<User> optionalDeleteTargetUser = userRepository.findById(userId);
        User deleteTargetUser = optionalDeleteTargetUser.orElseThrow(
                () -> new NotFoundException(UserErrorMessage.USER_NOT_FOUND)
        );

        String deleteReason = userDeleteRequest.getDeleteReason();

        session.removeAttribute("LOGIN_USER_ID");
        session.removeAttribute("LOGIN_EXPIRES_AT");    //탈퇴 성공 시 로그인 정보 삭제

        deleteTargetUser.deleteUser(deleteReason);
    }
}
