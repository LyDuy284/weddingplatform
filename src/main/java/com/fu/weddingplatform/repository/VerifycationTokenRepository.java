package com.fu.weddingplatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.VerificationToken;

public interface VerifycationTokenRepository extends JpaRepository<VerificationToken, Integer> {
  public Optional<VerificationToken> findByToken(String token);
}
