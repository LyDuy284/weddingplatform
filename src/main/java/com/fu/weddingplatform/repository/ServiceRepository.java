package com.fu.weddingplatform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.entity.Category;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.entity.Services;

@Repository
@Transactional
public interface ServiceRepository extends JpaRepository<Services, String> {

    public Page<Services> findByStatus(PageRequest pageRequest, String status);

    public Page<Services> findByServiceSupplier(PageRequest pageRequest, ServiceSupplier serviceSupplier);

    public Page<Services> findByCategory(PageRequest pageRequest, Category category);

    public Page<Services> findByCategoryAndServiceSupplier(PageRequest pageRequest, Category category,
            ServiceSupplier serviceSupplier);
}
