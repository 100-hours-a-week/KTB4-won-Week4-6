package com.example.KTB_assignment_week4.repository;

import com.example.KTB_assignment_week4.domain.User;
import com.example.KTB_assignment_week4.domain.UserRole;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JavaUserRepository implements UserRepository{
    private List<User> userData = new ArrayList<>();

    public void initiateUserData (){    //사용자 데이터 하드코딩
        userData.add(new User(1L,
                "사과",
                "apple@naver.com",
                "ilikeapple",
                UserRole.USER,
                false));
        userData.add(new User(2L,
                "바나나",
                "banana@naver.com",
                "ilikebanana",
                UserRole.USER,
                false));
        userData.add(new User(3L,
                "키위",
                "kiwi@naver.com",
                "ilikekiwi",
                UserRole.USER,
                false));
        userData.add(new User(4L,
                "망고",
                "mango@naver.com",
                "ilikemango",
                UserRole.USER,
                false));
    }

    public JavaUserRepository(){
        initiateUserData();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> userFindByEmail = userData.stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();

        return userFindByEmail;
    }
}
