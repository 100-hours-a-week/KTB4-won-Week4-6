package com.example.KTB_assignment_week4.repository;

import com.example.KTB_assignment_week4.domain.User;

import java.util.Optional;

public interface UserRepository {   //확장성 고려하여 인터페이스로 Repository구 JpaRepository구현 고려하여 메소드 명명규칙을 따름,
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);
    Long saveUser(User user);
    Optional<User> findByUserId(Long userId);
    void modifyUserInfo(Long userId, User modifiedUser);
    void modifyUserPassword(Long userId, User passwordModifiedPassword);
}
