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

        @Query(nativeQuery = true, value = "SELECT ss.* FROM the_day.service_supplier ss \n" +
                        "   join service s on ss.service_id = s.id \n" +
                        "   join category c on c.id = s.category_id \n" +
                        "where ( ?1 = '' or   c.id = ?1)   \n" +
                        "   and ( ?2 = '' or  s.id = ?2) \n" +
                        "   and ( ?7 = '' or  ss.status = ?7) \n" +
                        "   and ( ?3 = '' or  ss.type = ?3) \n" +
                        "   and (?4 <= ss.price and (?5 = 0 or ss.price <= ?5)) \n" +
                        "   and (?6 = '' or  ss.supplier_id = ?6)")
        public List<ServiceSupplier> filterServiceSupplier(String category, String service, String type, int minPrice,
                        int maxPrice, String supplierId, String status);

        @Query(nativeQuery = true, value = "SELECT ss.* FROM combo_service cs \n" +
                        "    join combo c on c.id = cs.combo_id \n" +
                        "    join service_supplier ss on ss.id = cs.service_supplier_id \n" +
                        "where cs.combo_id = ?1")
        List<ServiceSupplier> getServiceSupplierByCombo(String comboId);

        @Query(nativeQuery = true, value = "SELECT  count(distinct(id)) FROM the_day.booking_detail \n" + //
                        "where status not like 'COMPLETED' and status not like 'CANCELED' and status not like 'REJECTED' and service_supplier_id = ?1")
        int findForDisableServiceSupplier(String serviceSupplierId);

}
