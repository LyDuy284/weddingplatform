package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, String> {
    // Optional<Wallet> findByServiceSupplierId(String serviceSupplierId);
}
