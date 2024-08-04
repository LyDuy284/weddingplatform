package com.fu.weddingplatform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.entity.BlogPost;

@Repository
@Transactional
public interface BlogPostRepository extends JpaRepository<BlogPost, String> {

    // Page<BlogPost> findByServiceSupplier(ServiceSupplier serviceSupplier, Pageable pageable);

    Page<BlogPost> findByStatus(String status, Pageable pageable);
}
