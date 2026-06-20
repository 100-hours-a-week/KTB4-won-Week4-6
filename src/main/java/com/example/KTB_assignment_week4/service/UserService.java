package com.example.KTB_assignment_week4.service;

import com.example.KTB_assignment_week4.domain.User;
import com.example.KTB_assignment_week4.domain.UserRole;
import com.example.KTB_assignment_week4.dto.userDTO.Request.UserInfoModifyRequest;
import com.example.KTB_assignment_week4.dto.userDTO.Request.UserLoginRequest;
import com.example.KTB_assignment_week4.dto.userDTO.Request.UserPasswordModifyRequest;
import com.example.KTB_assignment_week4.dto.userDTO.Request.UserSignupRequest;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserInfoResponse;
import com.example.KTB_assignment_week4.dto.userDTO.Response.UserLoginResponse;
import com.example.KTB_assignment_week4.exception.LoginFailedException;
import com.example.KTB_assignment_week4.exception.NotFoundException;
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

    public UserLoginResponse login(@Valid UserLoginRequest userLoginRequest){
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        Optional<User> optionalUserFoundByEmail = userRepository.findByEmail(email);
        User userFoundByEmail = optionalUserFoundByEmail.orElseThrow(
                () -> new NotFoundException("존재하지 않는 사용자입니다."));
        String passwordOfFoundUser = userFoundByEmail.getPassWord();
        if(!passwordOfFoundUser.equals(password)){
            throw new LoginFailedException();
        }
        return UserLoginResponse.of(userFoundByEmail);
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

    public String modifyUserInfo(Long userId, UserInfoModifyRequest userInfoModifyRequest) throws NoSuchElementException{
        String newNickname = userInfoModifyRequest.getNickname();
        String newProfileImage = userInfoModifyRequest.getProfileImage();

        Optional<User> optionalModifyTargetUser = userRepository.findByUserId(userId);
        User modifyTargetUser = optionalModifyTargetUser.orElseThrow(NoSuchElementException::new);
        modifyTargetUser.setNickname(newNickname);
        modifyTargetUser.setProfileImage(newProfileImage);

        userRepository.modifyUserInfo(userId, modifyTargetUser);

        return "사용자 정보 수정 완료";
    }

    public String modifyUserPassword(Long userId, UserPasswordModifyRequest userPasswordModifyRequest) throws NoSuchElementException{
        String modifiedPassword = userPasswordModifyRequest.getPassword();

        Optional<User> optionalPasswordModifyTargetUser = userRepository.findByUserId(userId);
        User passwordModifyTargetUser = optionalPasswordModifyTargetUser.orElseThrow(NoSuchElementException::new);
        passwordModifyTargetUser.setPassWord(modifiedPassword);

        userRepository.modifyUserPassword(userId, passwordModifyTargetUser);

        return "비밀번호 변경 성공";
    }

    public String deleteUser(Long userId){
        Optional<User> optionalDeleteTargetUser = userRepository.findByUserId(userId);
        User deleteTargetUser = optionalDeleteTargetUser.orElseThrow(NoSuchElementException::new);
        deleteTargetUser.setIsDeleted(true);

        userRepository.deleteUser(userId, deleteTargetUser);

        return "사용자 삭제 성공";
    }
}
