package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, String> {

}
