package com.fu.weddingplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.entity.ServiceSupplier;

@Repository
@Transactional
public interface ServiceSupplierRepository extends JpaRepository<ServiceSupplier, String> {

    @Query(value = "SELECT c.id as categoryId, s.id as serviceId, ss.id as serviceSupplierId " +
            "FROM service_supplier ss " +
            "JOIN service s ON ss.service_id = s.id " +
            "JOIN category c ON c.id = s.category_id " +
            "WHERE ss.supplier_id = :supplierId AND ss.status = 'ACTIVATED' " +
            "GROUP BY c.id, s.id, ss.id " +
            "ORDER BY c.id", nativeQuery = true)
    public List<Object[]> getBySupplier(String supplierId);

}
