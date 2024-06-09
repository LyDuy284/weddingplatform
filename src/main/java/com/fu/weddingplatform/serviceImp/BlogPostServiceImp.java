package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.SupplierErrorMessage;
import com.fu.weddingplatform.constant.blogPost.BlogErrorMessage;
import com.fu.weddingplatform.entity.BlogPost;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.exception.EmptyException;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BlogPostRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.repository.StaffRepository;
import com.fu.weddingplatform.request.BlogPost.CreateBlogDTO;
import com.fu.weddingplatform.request.BlogPost.UpdateBlogDTO;
import com.fu.weddingplatform.response.BlogPost.BlogPostResponse;
import com.fu.weddingplatform.service.BlogPostService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BlogPostServiceImp implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final ModelMapper modelMapper;
    private final StaffRepository staffRepository;
    private final ServiceSupplierRepository serviceSupplierRepository;


    @Override
    public List<BlogPostResponse> getAllBlogPosts(int pageNo, int pageSize) {
        List<BlogPostResponse> response = new ArrayList<BlogPostResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findAll(pageable);

        if (!pageResult.hasContent()){
            throw new EmptyException(BlogErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()){
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            if (blogPost.getStaff() != null){
                blogPostResponse.setStaffId(blogPost.getStaff().getId());
            }
            blogPostResponse.setServiceSupplierId(blogPost.getServiceSupplier().getId());
            response.add(blogPostResponse);
        }
        return response;
    }

    @Override
    public List<BlogPostResponse> getAllPendingBlogPosts(int pageNo, int pageSize) {
        List<BlogPostResponse> response = new ArrayList<BlogPostResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findAll(pageable);
        
        if (!pageResult.hasContent()){
            throw new EmptyException(BlogErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()){
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            if (blogPost.getStatus().equalsIgnoreCase("PENDING")){
                response.add(blogPostResponse);
            }
        }
        return response;
    }

    @Override
    public List<BlogPostResponse> getAllActiveBlogPosts(int pageNo, int pageSize) {
        List<BlogPostResponse> response = new ArrayList<BlogPostResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findAll(pageable);
        
        if (!pageResult.hasContent()){
            throw new EmptyException(BlogErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()){
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            if (blogPost.getStatus().equalsIgnoreCase("ACTIVATED")){
                response.add(blogPostResponse);
            }
        }
        return response;
    }

    @Override
    public List<BlogPostResponse> getAllRejectedBlogPosts(int pageNo, int pageSize) {
        List<BlogPostResponse> response = new ArrayList<BlogPostResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findAll(pageable);
        
        if (!pageResult.hasContent()){
            throw new EmptyException(BlogErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()){
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            if (blogPost.getStatus().equalsIgnoreCase("REJECTED")){
                response.add(blogPostResponse);
            }
        }
        return response;
    }

    @Override
    public List<BlogPostResponse> getAllBlogPostsByServiceSupplier(String serviceSupplierId, int pageNo, int pageSize) {
        List<BlogPostResponse> response = new ArrayList<BlogPostResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findAll(pageable);
        
        if (!pageResult.hasContent()){
            throw new EmptyException(BlogErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()){
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            if (blogPost.getServiceSupplier().getId().equalsIgnoreCase(serviceSupplierId)){
                response.add(blogPostResponse);
            }
        }
        return response;
    }

    @Override
    public BlogPostResponse getBlogPostById(String id) {
        BlogPost blogPost = blogPostRepository.findById(Integer.parseInt(id)).orElseThrow(
                () -> new ErrorException(BlogErrorMessage.NOT_FOUND)
        );
        BlogPostResponse response = new BlogPostResponse(id, blogPost.getTitle(), blogPost.getContent(), blogPost.getDateCreated(), blogPost.getServiceSupplier().getId(), blogPost.getStaff().getId(), blogPost.getStatus());
        return response;
    }

    @Override
    public BlogPostResponse createBlogPost(CreateBlogDTO createDTO) {

        BlogPostResponse response = new BlogPostResponse();

        ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(createDTO.getServiceSupplierId()).orElseThrow(
                () -> new ErrorException(SupplierErrorMessage.NOT_FOUND)
        );

        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);

        BlogPost blog = new BlogPost().builder()
                .title(createDTO.getTitle())
                .content(createDTO.getContent())
                .dateCreated(localDateTime.format(dateTimeFormatter))
                .status(Status.PENDING)
                .serviceSupplier(serviceSupplier)
                .build();

        BlogPost newBlog = blogPostRepository.save(blog);

        response = modelMapper.map(newBlog, BlogPostResponse.class);
        response.setServiceSupplierId(serviceSupplier.getId());
        return response;
    }

    @Override
    public BlogPostResponse updateBlogPost(UpdateBlogDTO updateDTO) {
        return null;
    }

    @Override
    public BlogPostResponse approveBlogPost(String id, String staffId) {
        return null;
    }

    @Override
    public BlogPostResponse rejectBlogPost(String id, String staffId) {
        return null;
    }

    @Override
    public BlogPostResponse deleteBlogPost(String id) {
        return null;
    }
}
