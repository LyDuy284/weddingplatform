package com.fu.weddingplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.entity.Category;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, String> {

  @Query(nativeQuery = true, value = "SELECT category_name FROM wedding_platform.category \n" +
      "where status = 'ACTIVATED'")
  List<String> getCategoryName();

}
