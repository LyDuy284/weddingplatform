package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.request.blogPost.CreateBlogDTO;
import com.fu.weddingplatform.request.blogPost.UpdateBlogDTO;
import com.fu.weddingplatform.response.BlogPost.BlogPostResponse;

public interface BlogPostService {
    public List<BlogPostResponse> getAllBlogPosts(int pageNo, int pageSize);

    public List<BlogPostResponse> getAllActiveBlogPosts(int pageNo, int pageSize);

    public BlogPostResponse getBlogPostById(String id);

    public BlogPostResponse updateBlogPost(UpdateBlogDTO updateBlogDTO);

    public BlogPostResponse createBlogPost(CreateBlogDTO createDTO);

    public boolean deleteBlogPost(String id);

}
