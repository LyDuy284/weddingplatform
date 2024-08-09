package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.Wallet;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, String> {
    // Optional<Wallet> findByServiceSupplierId(String serviceSupplierId);
    @Query("select w from Wallet w where w.account.id = ?1")
    Optional<Wallet> findByAccountId(int accountId);
}
