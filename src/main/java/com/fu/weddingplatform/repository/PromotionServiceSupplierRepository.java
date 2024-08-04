package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.PromotionServiceSupplier;

@Repository
public interface PromotionServiceSupplierRepository extends JpaRepository<PromotionServiceSupplier, String> {

  // public List<PromotionServiceEntity> findByPromotion(Promotion protion);

  // public List<PromotionServiceEntity> findByService(Services service);

  // PromotionServiceEntity findByServiceAndPromotion(Services service, Promotion promotion);

}
