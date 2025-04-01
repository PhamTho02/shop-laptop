package com.example.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);

    Boolean existsByEmail(String email);

    User findByEmail(String email);
}
