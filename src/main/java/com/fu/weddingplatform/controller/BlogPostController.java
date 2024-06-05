package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.account.AccountSuccessMessage;
import com.fu.weddingplatform.constant.blogPost.BlogSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.request.BlogPost.CreateBlogDTO;
import com.fu.weddingplatform.response.Account.AccountResponse;
import com.fu.weddingplatform.response.BlogPost.BlogPostResponse;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @GetMapping("getAllBlogPosts")
    public ResponseEntity<ListResponseDTO> getAllBlogPosts(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
        ListResponseDTO<BlogPostResponse> responseDTO = new ListResponseDTO();
        List<BlogPostResponse> list = blogPostService.getAllBlogPosts(pageNo, pageSize);
        responseDTO.setData(list);
        responseDTO.setMessage(BlogSuccessMessage.GET_ALL);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("create")
    @PreAuthorize(RolePreAuthorize.ROLE_SERVICE_SUPPLIER)
    public ResponseEntity<ResponseDTO> createBlogPost(@RequestBody CreateBlogDTO createBlogDTO){
        ResponseDTO<BlogPostResponse> responseDTO = new ResponseDTO<>();
        BlogPostResponse data = blogPostService.createBlogPost(createBlogDTO);
        responseDTO.setData(data);
        responseDTO.setMessage(BlogSuccessMessage.CREATE);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
