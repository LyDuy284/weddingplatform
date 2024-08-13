package com.fu.weddingplatform.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.Promotion;
import com.fu.weddingplatform.entity.Supplier;

@Repository
@Transactional
public interface PromotionRepository extends JpaRepository<Promotion, String> {

        public List<Promotion> findBySupplierAndStatus(Supplier supplier, String status);

        @Query(nativeQuery = true, value = "SELECT * FROM promotion \n" + //
                        " where end_date < ?1 and status = 'ACTIVATED'")
        public List<Promotion> findExpriedPromotion(String currentDate);

}