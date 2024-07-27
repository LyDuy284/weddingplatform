package com.fu.weddingplatform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Quotation;
import com.fu.weddingplatform.entity.ServiceSupplier;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, String> {

  Page<Quotation> findByCouple(Couple couple, PageRequest request);

  Page<Quotation> findByServiceSupplier(ServiceSupplier serviceSupplier, PageRequest request);

}
