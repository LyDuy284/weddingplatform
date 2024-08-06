package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.TransactionSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionSummaryRepository extends JpaRepository<TransactionSummary, String> {
}
