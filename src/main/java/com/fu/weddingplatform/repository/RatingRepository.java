package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, String>, JpaSpecificationExecutor<Rating> {
    @Query("select r from Rating as r where r.id = ?1 and r.couple.id = ?2")
    Optional<Rating> findByIdAndCouple(String id, String coupleId);

    @Query(value = "SELECT IFNULL(AVG(r.rating), 0) as rating FROM rating r \n" +
            "   JOIN booking_detail bd on r.booking_detail_id = bd.id \n" +
            "   JOIN service_supplier ss on ss.id= bd.service_supplier_id \n" +
            "WHERE ss.id=?1 and bd.status='COMPLETED'", nativeQuery = true)
    float getRatingByServiceSupplier(String serviceSupplierId);
}
