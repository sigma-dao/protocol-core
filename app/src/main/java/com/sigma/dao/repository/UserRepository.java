package com.sigma.dao.repository;

import com.sigma.dao.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPublicKey(String publicKey);
}