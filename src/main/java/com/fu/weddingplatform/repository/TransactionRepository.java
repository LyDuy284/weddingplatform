package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    @Query("select t from Transaction t where t.payment.id = ?1")
    List<Transaction> findByPaymentId(String paymentId);
}
