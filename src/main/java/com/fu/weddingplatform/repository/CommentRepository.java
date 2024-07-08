package com.fu.weddingplatform.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.BlogPost;
import com.fu.weddingplatform.entity.Comment;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, String> {
  Page<Comment> findByParentIsNullAndBlogPost(BlogPost blogPost, PageRequest pageRequest);
}
