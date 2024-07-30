package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ComboRepository extends JpaRepository<Combo, String>, JpaSpecificationExecutor<Combo> {
}
