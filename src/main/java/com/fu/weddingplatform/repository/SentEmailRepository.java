package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.SentEmail;

public interface SentEmailRepository extends JpaRepository<SentEmail, Integer> {
  SentEmail findFirstByStatus(String status);
}
