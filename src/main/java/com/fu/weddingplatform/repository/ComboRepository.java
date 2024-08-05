package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fu.weddingplatform.entity.Combo;

public interface ComboRepository extends JpaRepository<Combo, String>, JpaSpecificationExecutor<Combo> {
}
