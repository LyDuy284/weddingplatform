package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
}
