package com.fu.weddingplatform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.entity.Services;

@Repository
@Transactional
public interface ServiceRepository extends JpaRepository<Services, String> {
    Page<Services> findByStatus(PageRequest pageRequest, String status);
}
