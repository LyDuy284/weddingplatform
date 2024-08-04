package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.PromotionServiceSupplier;
import com.fu.weddingplatform.entity.ServiceSupplier;

@Repository
public interface PromotionServiceSupplierRepository extends JpaRepository<PromotionServiceSupplier, String> {

  PromotionServiceSupplier findFirstByServiceSupplierAndStatus(ServiceSupplier serviceSupplier, String status);

}
