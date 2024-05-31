package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CoupleRepository extends JpaRepository<Couple, Integer> {
    Optional<Couple> findById(int coupleId);
}
