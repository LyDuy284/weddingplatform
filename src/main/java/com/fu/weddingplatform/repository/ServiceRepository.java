package com.fu.weddingplatform.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.entity.Category;
import com.fu.weddingplatform.entity.Services;

@Repository
@Transactional
public interface ServiceRepository extends JpaRepository<Services, String> {

    public Page<Services> findByStatus(PageRequest pageRequest, String status);

    public List<Services> findByStatus(String status);

    public List<Services> findByCategory(Category category);

    // public Page<Services> findByCategoryAndServiceSupplier(PageRequest
    // pageRequest, Category category,
    // ServiceSupplier serviceSupplier);

    @Query(nativeQuery = true, value = "SELECT s.* \n" +
            " FROM service s \n" +
            "   join category c on c.id = s.category_id \n" +
            " where category_id = ?1  \n" +
            "   AND (?2 = '' or s.type = ?2) \n" +
            "   AND s.price >= ?3 \n" +
            "   AND (?4 = 0 OR s.price <= ?4)")
    public List<Services> filterService(String categoryId, String type, int minPrice, int maxPrice);
}
