package com.example.KTB_assignment_week4.repository;

import com.example.KTB_assignment_week4.domain.User;

import java.util.Optional;

public interface UserRepository {   //확장성 고려하여 인터페이스로 Repository구현
    Optional<User> findByEmail(String email);
}
