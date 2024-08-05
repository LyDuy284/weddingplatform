package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, String> {
    // Optional<Wallet> findByServiceSupplierId(String serviceSupplierId);
}
