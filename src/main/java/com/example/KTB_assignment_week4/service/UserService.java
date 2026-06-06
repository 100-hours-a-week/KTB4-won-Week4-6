package com.example.KTB_assignment_week4.service;

import com.example.KTB_assignment_week4.domain.User;
import com.example.KTB_assignment_week4.domain.UserRole;
import com.example.KTB_assignment_week4.dto.*;
import com.example.KTB_assignment_week4.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String login(@Valid UserLoginRequest userLoginRequest) throws IllegalArgumentException{
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        Optional<User> userFindByEmail = userRepository.findByEmail(email); //하드코딩된 값이지만 DB 호출을 할 경우 null값이 있을 수 있으므로 Optional로 처리

        String passwordOfFindUser = userFindByEmail.orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다"))
                .getPassWord();
        if(passwordOfFindUser.equals(password)){
            return "로그인 성공";
        }
        else{
            return "로그인 실패";
        }
    }

    public void checkEmailDuplication(String email){        //이메일 중복체크 => 중복 시 예외처리
        if(userRepository.existsByEmail(Optional.ofNullable(email).toString())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        };
    }

    public void checkNicknameDuplication(String nickname){  //닉네임 중복체크 => 중복 시 예외처리
        if(userRepository.existsByNickname(Optional.ofNullable(nickname).toString())){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    public Long signup(@Valid UserSignupRequest userSignupRequest) throws IllegalArgumentException{
        Long userId = userSignupRequest.getUserId();
        String email = userSignupRequest.getEmail();
        String password = userSignupRequest.getPassword();
        String nickname = userSignupRequest.getNickname();
        String profileImage = userSignupRequest.getProfileImage();

        checkEmailDuplication(email);
        checkNicknameDuplication(nickname);  //이메일과 닉네임 중복 체크 후 중복된다면 예외 발생 => 예외 발생된다는 것을 코드만 보고 알기 어려운데 괜찮을까?

        User user = new User(userId, nickname, email, password, UserRole.USER, false, profileImage);

        return userRepository.saveUser(user);
    }

    public UserInfoResponse getUserInfo(Long userId) throws NoSuchElementException{
        Optional<User> userFoundById = userRepository.findByUserId(userId);
        String email = userFoundById.orElseThrow(NoSuchElementException::new).getEmail();
        String nickname = userFoundById.orElseThrow(NoSuchElementException::new).getNickname();
        String profileImage = userFoundById.orElseThrow(NoSuchElementException::new).getProfileImage();

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setEmail(email);
        userInfoResponse.setNickname(nickname);
        userInfoResponse.setProfileImage(profileImage);

        return userInfoResponse;
    }

    public void modifyUserInfo(Long userId, UserInfoModifyRequest userInfoModifyRequest) throws NoSuchElementException{
        String newNickname = userInfoModifyRequest.getNickname();
        String newProfileImage = userInfoModifyRequest.getProfileImage();

        Optional<User> optionalModifyTargetUser = userRepository.findByUserId(userId);
        User modifyTargetUser = optionalModifyTargetUser.orElseThrow(NoSuchElementException::new);
        modifyTargetUser.setNickname(newNickname);
        modifyTargetUser.setProfileImage(newProfileImage);

        userRepository.modifyUserInfo(userId, modifyTargetUser);
    }

    public void modifyUserPassword(Long userId, UserPasswordModifyRequest userPasswordModifyRequest){
        String modifiedPassword = userPasswordModifyRequest.getPassword();

        Optional<User> optionalPasswordModifyTargetUser = userRepository.findByUserId(userId);
        User passwordModifyTargetUser = optionalPasswordModifyTargetUser.orElseThrow(NoSuchElementException::new);
        passwordModifyTargetUser.setPassWord(modifiedPassword);

        userRepository.modifyUserPassword(userId, passwordModifyTargetUser);
    }
}
