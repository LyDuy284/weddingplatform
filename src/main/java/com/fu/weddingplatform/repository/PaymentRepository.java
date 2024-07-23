package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
