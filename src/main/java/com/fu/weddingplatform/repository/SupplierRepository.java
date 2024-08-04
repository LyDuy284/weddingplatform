package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, String>{
    
}
