package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.Payment;
import com.fu.weddingplatform.enums.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Query("select p from Payment as p where p.booking.id = ?1 and p.paymentType = ?2")
    Optional<Payment> findByBookingType(String bookingId, PaymentType type);
}
