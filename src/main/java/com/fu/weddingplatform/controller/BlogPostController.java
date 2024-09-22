package com.fu.weddingplatform.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.blogPost.BlogPostSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.request.blogPost.CreateBlogDTO;
import com.fu.weddingplatform.request.blogPost.UpdateBlogDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.BlogPost.BlogPostResponse;
import com.fu.weddingplatform.service.BlogPostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @GetMapping("getAllBlogPosts")
    public ResponseEntity<?> getAllBlogPosts(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        ListResponseDTO<BlogPostResponse> responseDTO = new ListResponseDTO<>();
        List<BlogPostResponse> list = blogPostService.getAllBlogPosts(pageNo, pageSize);
        responseDTO.setData(list);
        responseDTO.setMessage(BlogPostSuccessMessage.GET_ALL);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("create")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> createBlogPost(@Validated @RequestBody CreateBlogDTO createBlogDTO) {
        ResponseDTO<BlogPostResponse> responseDTO = new ResponseDTO<>();
        BlogPostResponse data = blogPostService.createBlogPost(createBlogDTO);
        responseDTO.setData(data);
        responseDTO.setMessage(BlogPostSuccessMessage.CREATE);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> updateBlogPost(@Validated @RequestBody UpdateBlogDTO updateDTO) {
        ResponseDTO<BlogPostResponse> responseDTO = new ResponseDTO<>();
        BlogPostResponse data = blogPostService.updateBlogPost(updateDTO);
        responseDTO.setData(data);
        responseDTO.setMessage(BlogPostSuccessMessage.UPDATE);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getAllActiveBlogPosts")
    public ResponseEntity<?> getAllActiveBlogPosts(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        ListResponseDTO<BlogPostResponse> responseDTO = new ListResponseDTO<>();
        List<BlogPostResponse> list = blogPostService.getAllActiveBlogPosts(pageNo, pageSize);
        responseDTO.setData(list);
        responseDTO.setMessage(BlogPostSuccessMessage.GET_ALL_ACTIVE);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getBlogPostById/{id}")
    public ResponseEntity<ResponseDTO<BlogPostResponse>> getBlogPostById(@PathVariable String id) {
        ResponseDTO<BlogPostResponse> responseDTO = new ResponseDTO<>();
        BlogPostResponse blogPostResponse = blogPostService.getBlogPostById(id);
        responseDTO.setData(blogPostResponse);
        responseDTO.setMessage(BlogPostSuccessMessage.GET_BY_ID);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("delete")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> deleteBlogPost(@RequestParam String id) {
        ResponseDTO<Boolean> responseDTO = new ResponseDTO<>();
        Boolean data = blogPostService.deleteBlogPost(id);
        responseDTO.setData(data);
        responseDTO.setMessage(BlogPostSuccessMessage.UPDATE);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
