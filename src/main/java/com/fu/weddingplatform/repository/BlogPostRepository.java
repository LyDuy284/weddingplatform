package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    Page<BlogPost> findByServiceSupplierId(String serviceSupplierId, Pageable pageable);
}
