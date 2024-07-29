package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, String>, JpaSpecificationExecutor<Rating> {
    @Query("select r from Rating as r where r.id = ?1 and r.couple.id = ?2")
    Optional<Rating> findByIdAndCouple(String id, String coupleId);
}
