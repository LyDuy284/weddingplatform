package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuotationRepository extends JpaRepository<Quotation, String> {
    Optional<Quotation> findById(String quotationId);
}
