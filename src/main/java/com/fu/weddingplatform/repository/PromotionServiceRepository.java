package com.fu.weddingplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.Promotion;
import com.fu.weddingplatform.entity.PromotionServiceEntity;
import com.fu.weddingplatform.entity.Services;

@Repository
public interface PromotionServiceRepository extends JpaRepository<PromotionServiceEntity, String> {

  public List<PromotionServiceEntity> findByPromotion(Promotion protion);

  public List<PromotionServiceEntity> findByService(Services service);

}
