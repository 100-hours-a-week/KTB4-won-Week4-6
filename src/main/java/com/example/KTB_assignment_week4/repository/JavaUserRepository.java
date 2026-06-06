package com.example.KTB_assignment_week4.repository;

import com.example.KTB_assignment_week4.domain.User;
import com.example.KTB_assignment_week4.domain.UserRole;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JavaUserRepository implements UserRepository{
    private final List<User> userData = new ArrayList<>();

    public void initiateUserData (){    //사용자 데이터 하드코딩
        userData.add(new User(1L,
                "사과",
                "apple@naver.com",
                "ilikeapple",
                UserRole.USER,
                false,
                ""));
        userData.add(new User(2L,
                "바나나",
                "banana@naver.com",
                "ilikebanana",
                UserRole.USER,
                false,
                ""));
        userData.add(new User(3L,
                "키위",
                "kiwi@naver.com",
                "ilikekiwi",
                UserRole.USER,
                false,
                ""));
        userData.add(new User(4L,
                "망고",
                "mango@naver.com",
                "ilikemango",
                UserRole.USER,
                false,
                ""));
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

    @Override
    public Boolean existsByEmail(String email) {    //이메일 중복확인 메소드 구현
        return findByEmail(email).isPresent();
    }

    @Override
    public Boolean existsByNickname(String nickname) {  //닉네임 중복확인 메소드 구현
        Optional<User> userFindByNickname = userData.stream()
                .filter(user -> user.getNickname().equals(nickname))
                .findAny();
        return userFindByNickname.isPresent();
    }

    @Override
    public Long saveUser(User user) {
        userData.add(user);
        return user.getUserId();
    }

    @Override
    public Optional<User> findByUserId(Long userId){
        Optional<User> foundUser = userData.stream()
                .filter(user -> Objects.equals(user.getUserId(), userId))
                .findAny();

        return foundUser;
    }

    @Override
    public void modifyUserInfo(Long userId, User modifiedUser) {
        Optional<User> modifyTargetUser = findByUserId(userId);
        int indexOfFoundUser = userData.indexOf(modifyTargetUser.orElseThrow(NoSuchElementException::new));
        userData.set(indexOfFoundUser, modifiedUser);
    }

    @Override
    public void modifyUserPassword(Long userId, User passwordModifiedUser) {
        Optional<User> passwordModifyTargetUser = findByUserId(userId);
        int indexOfFoundUser = userData.indexOf(passwordModifyTargetUser.orElseThrow(NoSuchElementException::new));
        userData.set(indexOfFoundUser, passwordModifiedUser);
    }
}
