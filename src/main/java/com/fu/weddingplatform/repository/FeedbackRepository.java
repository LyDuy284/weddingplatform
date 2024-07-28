package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, String>, JpaSpecificationExecutor<Feedback> {
    @Query("select f from Feedback as f where f.id = ?1 and f.couple.id = ?2 and f.serviceSupplier.id = ?3")
    Optional<Feedback> findFeedbackByCoupleAndServiceSupplier(String id, String coupleId, String supplierId);
}
