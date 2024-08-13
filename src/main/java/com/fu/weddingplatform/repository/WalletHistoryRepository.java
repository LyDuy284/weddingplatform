package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WalletHistoryRepository extends JpaRepository<WalletHistory, String> , JpaSpecificationExecutor<WalletHistory> {
}
