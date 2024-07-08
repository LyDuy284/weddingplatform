package com.fu.weddingplatform.service;

import com.fu.weddingplatform.request.blogPost.CreateBlogDTO;
import com.fu.weddingplatform.request.blogPost.UpdateBlogDTO;
import com.fu.weddingplatform.response.BlogPost.BlogPostResponse;

import java.util.List;

public interface BlogPostService {
    public List<BlogPostResponse> getAllBlogPosts(int pageNo, int pageSize);

    public List<BlogPostResponse> getAllPendingBlogPosts(int pageNo, int pageSize);

    public List<BlogPostResponse> getAllActiveBlogPosts(int pageNo, int pageSize);

    public List<BlogPostResponse> getAllRejectedBlogPosts(int pageNo, int pageSize);

    public List<BlogPostResponse> getAllBlogPostsByServiceSupplier(String serviceSupplierId, int pageNo, int pageSize);

    public BlogPostResponse getBlogPostById(String id);

    public BlogPostResponse createBlogPost(CreateBlogDTO createDTO);

    public BlogPostResponse updateBlogPost(UpdateBlogDTO updateDTO);

    public BlogPostResponse approveBlogPost(String id, String staffId);

    public BlogPostResponse rejectBlogPost(String id, String staffId);

    public BlogPostResponse deleteBlogPost(String id);

}
