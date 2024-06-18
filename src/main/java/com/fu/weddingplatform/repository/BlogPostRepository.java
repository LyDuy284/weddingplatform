package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.BlogPost;
import com.fu.weddingplatform.entity.ServiceSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {

    Page<BlogPost> findByServiceSupplier(ServiceSupplier serviceSupplier, Pageable pageable);

    Page<BlogPost> findByStatus(String status, Pageable pageable);
}
