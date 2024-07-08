package com.fu.weddingplatform.serviceImp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.blogPost.BlogPostErrorMessage;
import com.fu.weddingplatform.constant.serviceSupplier.SupplierErrorMessage;
import com.fu.weddingplatform.entity.BlogPost;
import com.fu.weddingplatform.entity.Comment;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.exception.EmptyException;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BlogPostRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.request.blogPost.CreateBlogDTO;
import com.fu.weddingplatform.request.blogPost.UpdateBlogDTO;
import com.fu.weddingplatform.response.BlogPost.BlogPostResponse;
import com.fu.weddingplatform.response.comment.CommentResponse;
import com.fu.weddingplatform.service.BlogPostService;
import com.fu.weddingplatform.service.CommentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlogPostServiceImp implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final ModelMapper modelMapper;
    private final ServiceSupplierRepository serviceSupplierRepository;
    private final CommentService commentService;

    @Override
    public List<BlogPostResponse> getAllBlogPosts(int pageNo, int pageSize) {
        List<BlogPostResponse> response = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findAll(pageable);

        if (!pageResult.hasContent()) {
            throw new EmptyException(BlogPostErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()) {
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getStaff().getId());
            }
            blogPostResponse.setServiceSupplierId(blogPost.getServiceSupplier().getId());

            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getServiceSupplier().getId());
            }

            List<String> listImages = new ArrayList<String>();
            if (blogPost.getImages() != null || blogPost.getImages() != "") {
                String[] imageArray = blogPost.getImages().split("\n,");
                for (String image : imageArray) {
                    listImages.add(image.trim());
                }
            }
            blogPostResponse.setListImages(listImages);

            List<CommentResponse> listComments = new ArrayList<CommentResponse>();
            for (Comment comment : blogPost.getComments()) {
                CommentResponse commentResponse = commentService.getCommentById(comment.getId());
                listComments.add(commentResponse);
            }
            blogPostResponse.setListComments(listComments);
            blogPostResponse.setCreateAt(blogPost.getDateCreated());
            response.add(blogPostResponse);
        }
        return response;
    }

    @Override
    public List<BlogPostResponse> getAllPendingBlogPosts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findByStatus(Status.PENDING, pageable);

        List<BlogPostResponse> response = new ArrayList<>();
        if (!pageResult.hasContent()) {
            throw new EmptyException(BlogPostErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()) {
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getStaff().getId());
            }
            blogPostResponse.setServiceSupplierId(blogPost.getServiceSupplier().getId());

            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getServiceSupplier().getId());
            }

            List<String> listImages = new ArrayList<String>();
            if (blogPost.getImages() != null || blogPost.getImages() != "") {
                String[] imageArray = blogPost.getImages().split("\n,");
                for (String image : imageArray) {
                    listImages.add(image.trim());
                }
            }
            blogPostResponse.setListImages(listImages);

            List<CommentResponse> listComments = new ArrayList<CommentResponse>();
            for (Comment comment : blogPost.getComments()) {
                CommentResponse commentResponse = commentService.getCommentById(comment.getId());
                listComments.add(commentResponse);
            }
            blogPostResponse.setCreateAt(blogPost.getDateCreated());
            blogPostResponse.setListComments(listComments);
            response.add(blogPostResponse);
        }
        return response;
    }

    @Override
    public List<BlogPostResponse> getAllActiveBlogPosts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findByStatus(Status.ACTIVATED, pageable);

        List<BlogPostResponse> response = new ArrayList<>();
        if (!pageResult.hasContent()) {
            throw new EmptyException(BlogPostErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()) {
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getStaff().getId());
            }
            blogPostResponse.setServiceSupplierId(blogPost.getServiceSupplier().getId());

            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getServiceSupplier().getId());
            }

            List<String> listImages = new ArrayList<String>();
            if (blogPost.getImages() != null || blogPost.getImages() != "") {
                String[] imageArray = blogPost.getImages().split("\n,");
                for (String image : imageArray) {
                    listImages.add(image.trim());
                }
            }
            blogPostResponse.setListImages(listImages);

            List<CommentResponse> listComments = new ArrayList<CommentResponse>();
            for (Comment comment : blogPost.getComments()) {
                CommentResponse commentResponse = commentService.getCommentById(comment.getId());
                listComments.add(commentResponse);
            }
            blogPostResponse.setCreateAt(blogPost.getDateCreated());
            blogPostResponse.setListComments(listComments);
            response.add(blogPostResponse);
        }
        return response;
    }

    @Override
    public List<BlogPostResponse> getAllRejectedBlogPosts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findByStatus(Status.REJECTED, pageable);

        List<BlogPostResponse> response = new ArrayList<>();
        if (!pageResult.hasContent()) {
            throw new EmptyException(BlogPostErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()) {
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getStaff().getId());
            }
            blogPostResponse.setServiceSupplierId(blogPost.getServiceSupplier().getId());

            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getServiceSupplier().getId());
            }

            List<String> listImages = new ArrayList<String>();
            if (blogPost.getImages() != null || blogPost.getImages() != "") {
                String[] imageArray = blogPost.getImages().split("\n,");
                for (String image : imageArray) {
                    listImages.add(image.trim());
                }
            }
            blogPostResponse.setListImages(listImages);

            List<CommentResponse> listComments = new ArrayList<CommentResponse>();
            for (Comment comment : blogPost.getComments()) {
                CommentResponse commentResponse = commentService.getCommentById(comment.getId());
                listComments.add(commentResponse);
            }
            blogPostResponse.setCreateAt(blogPost.getDateCreated());
            blogPostResponse.setListComments(listComments);
            response.add(blogPostResponse);
        }
        return response;
    }

    @Override
    public List<BlogPostResponse> getAllBlogPostsByServiceSupplier(String serviceSupplierId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(serviceSupplierId).orElseThrow(
                () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));
        Page<BlogPost> pageResult = blogPostRepository.findByServiceSupplier(serviceSupplier, pageable);

        List<BlogPostResponse> response = new ArrayList<>();
        if (!pageResult.hasContent()) {
            throw new EmptyException(BlogPostErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()) {
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getStaff().getId());
            }
            blogPostResponse.setServiceSupplierId(blogPost.getServiceSupplier().getId());

            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getServiceSupplier().getId());
            }

            List<String> listImages = new ArrayList<String>();
            if (blogPost.getImages() != null || blogPost.getImages() != "") {
                String[] imageArray = blogPost.getImages().split("\n,");
                for (String image : imageArray) {
                    listImages.add(image.trim());
                }
            }
            blogPostResponse.setListImages(listImages);

            List<CommentResponse> listComments = new ArrayList<CommentResponse>();
            for (Comment comment : blogPost.getComments()) {
                CommentResponse commentResponse = commentService.getCommentById(comment.getId());
                listComments.add(commentResponse);
            }
            blogPostResponse.setCreateAt(blogPost.getDateCreated());
            blogPostResponse.setListComments(listComments);
            response.add(blogPostResponse);
        }
        return response;
    }

    @Override
    public BlogPostResponse getBlogPostById(String id) {
        BlogPost blogPost = blogPostRepository.findById(id).orElseThrow(
                () -> new ErrorException(BlogPostErrorMessage.NOT_FOUND));

        BlogPostResponse response;
        response = modelMapper.map(blogPost, BlogPostResponse.class);
        response.setServiceSupplierId(blogPost.getServiceSupplier().getId());

        if (blogPost.getStaff() != null) {
            response.setStaffId(blogPost.getServiceSupplier().getId());
        }

        List<String> listImages = new ArrayList<String>();
        if (blogPost.getImages() != null || blogPost.getImages() != "") {
            String[] imageArray = blogPost.getImages().split("\n,");
            for (String image : imageArray) {
                listImages.add(image.trim());
            }
        }
        response.setListImages(listImages);

        List<CommentResponse> listComments = new ArrayList<CommentResponse>();
        for (Comment comment : blogPost.getComments()) {
            CommentResponse commentResponse = commentService.getCommentById(comment.getId());
            listComments.add(commentResponse);
        }
        response.setCreateAt(blogPost.getDateCreated());
        response.setListComments(listComments);

        return response;
    }

    @Override
    public BlogPostResponse createBlogPost(CreateBlogDTO createDTO) {
        BlogPostResponse response = new BlogPostResponse();

        ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(createDTO.getServiceSupplierId())
                .orElseThrow(
                        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);

        BlogPost blog = BlogPost.builder()
                .title(createDTO.getTitle())
                .content(createDTO.getContent())
                .dateCreated(localDateTime.format(dateTimeFormatter))
                .images(createDTO.getImages())
                .status(Status.PENDING)
                .serviceSupplier(serviceSupplier)
                .build();

        BlogPost newBlog = blogPostRepository.save(blog);

        response = modelMapper.map(newBlog, BlogPostResponse.class);

        List<String> listImages = new ArrayList<String>();
        if (newBlog.getImages() != null || newBlog.getImages() != "") {
            String[] imageArray = newBlog.getImages().split("\n,");
            for (String image : imageArray) {
                listImages.add(image.trim());
            }
        }
        response.setListImages(listImages);
        response.setListComments(new ArrayList<>());
        response.setCreateAt(newBlog.getDateCreated());
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
