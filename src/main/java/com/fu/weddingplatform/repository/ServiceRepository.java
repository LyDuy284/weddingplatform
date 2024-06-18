package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ServiceRepository extends JpaRepository<Services, Integer> {


}
