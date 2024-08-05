package com.fu.weddingplatform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.entity.Couple;

@Repository
@Transactional
public interface CoupleRepository extends JpaRepository<Couple, String> {
    Optional<Couple> findById(String coupleId);
}
