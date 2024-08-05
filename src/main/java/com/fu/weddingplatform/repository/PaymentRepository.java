package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
