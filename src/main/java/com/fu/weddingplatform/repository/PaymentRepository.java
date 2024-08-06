package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.Payment;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Query("select p from Payment p where p.invoice.id = ?1 and p.paymentMethod = ?2")
    Payment findByInvoiceIdAndPaymentMethod(String invoiceId, PaymentMethod paymentMethod);
}
