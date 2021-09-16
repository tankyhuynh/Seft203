package com.kms.seft203.domain.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

}
