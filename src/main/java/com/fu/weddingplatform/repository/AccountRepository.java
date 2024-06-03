package com.fu.weddingplatform.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.entity.Role;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Integer> {

    public Optional<Account> findByEmail(String email);

    public Optional<Account> findByEmailAndProvider(String email, String provider);

    public Page<Account> findAll(Pageable pageable);

    public boolean existsByRole(Role role);

    public Page<Account> findByRole(Role role, Pageable pageable);

    public Page<Account> findByStatus(String status, Pageable pageable);

}
