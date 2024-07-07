package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.entity.Category;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, String> {

}
