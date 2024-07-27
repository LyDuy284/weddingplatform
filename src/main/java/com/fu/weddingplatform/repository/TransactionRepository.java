package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
