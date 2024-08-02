package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.Payment;
import com.fu.weddingplatform.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
