package com.example.KTB_assignment_week4.repository;

import com.example.KTB_assignment_week4.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {   //확장성 고려하여 인터페이스로 Repository구 JpaRepository구현 고려하여 메소드 명명규칙을 따름,
    Optional<User> findByEmailAndIsDeletedFalse(String email);
    Boolean existsByEmailAndIsDeletedFalse(String email);
    Boolean existsByNicknameAndIsDeletedFalse(String nickname);
}
