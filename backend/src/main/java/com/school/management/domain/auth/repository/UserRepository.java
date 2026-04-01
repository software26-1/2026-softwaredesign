package com.school.management.domain.auth.repository;


import com.school.management.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository<User, Long> 상속만 해도 save(), findById(), findAll(), delete() 등 기본 CRUD가 자동으로 생김
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    // existsByEmail → SELECT COUNT(*) FROM users WHERE email = ?
    boolean existsByEmail(String email);
}
