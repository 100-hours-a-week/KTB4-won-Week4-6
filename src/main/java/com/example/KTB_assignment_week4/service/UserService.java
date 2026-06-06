package com.example.KTB_assignment_week4.service;

import com.example.KTB_assignment_week4.domain.User;
import com.example.KTB_assignment_week4.dto.UserLoginRequest;
import com.example.KTB_assignment_week4.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    public final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String login(UserLoginRequest userLoginRequest) throws IllegalArgumentException{
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        Optional<User> userFindByEmail = userRepository.findByEmail(email);

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
}
