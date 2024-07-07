package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ServiceRepository extends JpaRepository<Services, String> {

    @Query(nativeQuery = true, value = "SELECT RIGHT(id, LENGTH(id) - LOCATE('-', id)) AS number\n" +
            "FROM service\n" +
            "ORDER BY id DESC\n" +
            "LIMIT 1;")
    int getMaxId();

}
