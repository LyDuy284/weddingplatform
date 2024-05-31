package com.fu.weddingplatform.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.entity.Role;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Integer> {

    public Optional<Account> findAccountByEmail(String email);

    public Account findByEmail(String email);

    public boolean existsByRole(Role role);

    public Page<Account> findByRole(Role role, Pageable pageable);

    public Page<Account> findByStatus(String status, Pageable pageable);

}
