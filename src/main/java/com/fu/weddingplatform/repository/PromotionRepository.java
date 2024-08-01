package com.fu.weddingplatform.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.Promotion;
import com.fu.weddingplatform.entity.ServiceSupplier;

@Repository
@Transactional
public interface PromotionRepository extends JpaRepository<Promotion, String> {

        public Page<Promotion> findByServiceSupplierAndStatus(ServiceSupplier serviceSupplier, String status,
                        PageRequest pageRequest);

        @Query(nativeQuery = true, value = "SELECT p.*\n" +
                        " FROM promotion p \n" +
                        " JOIN promotion_service ps ON p.id = ps.promotion_id \n" +
                        " JOIN service s ON ps.service_id = s.id \n" +
                        " WHERE s.id = ?1 and (?2 BETWEEN p.start_date AND p.end_date) \n" +
                        "AND p.status = 'ACTIVATED' \n" +
                        " limit ?4 offset ?3")
        public List<Promotion> findByService(String serviceId, String currentDate, int pageNo, int pageSize);

        @Query(nativeQuery = true, value = "SELECT p.*\n" +
                        " FROM promotion p \n" +
                        " JOIN promotion_service ps ON p.id = ps.promotion_id \n" +
                        " JOIN service s ON ps.service_id = s.id \n" +
                        " WHERE s.id = ?1 and (?2 BETWEEN p.start_date AND p.end_date) \n" +
                        "AND p.status = 'ACTIVATED' AND ps.status =  'ACTIVATED' \n" +
                        "            limit 1\n")
        public Promotion findByService(String serviceId, String currentDate);

        @Query(nativeQuery = true, value = "SELECT p.* \n" +
                        " FROM promotion p  \n" +
                        "	JOIN service_supplier ss ON p.service_supplier_id = ss.id  \n" +
                        " WHERE ss.id = ?1 \n" +
                        "    and (?2 BETWEEN p.start_date AND p.end_date) \n" +
                        "	AND p.status = 'ACTIVATED'")
        public List<Promotion> findBySupplier(String supplierId, String currentDate);

}
