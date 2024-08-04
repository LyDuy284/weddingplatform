package com.fu.weddingplatform.serviceImp;

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
import com.fu.weddingplatform.constant.staff.StaffErrorMessage;
import com.fu.weddingplatform.entity.BlogPost;
import com.fu.weddingplatform.entity.Staff;
import com.fu.weddingplatform.exception.EmptyException;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BlogPostRepository;
import com.fu.weddingplatform.repository.StaffRepository;
import com.fu.weddingplatform.request.blogPost.CreateBlogDTO;
import com.fu.weddingplatform.request.blogPost.UpdateBlogDTO;
import com.fu.weddingplatform.response.BlogPost.BlogPostResponse;
import com.fu.weddingplatform.service.BlogPostService;
import com.fu.weddingplatform.utils.Utils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlogPostServiceImp implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final ModelMapper modelMapper;
    private final StaffRepository staffRepository;

    @Override
    public List<BlogPostResponse> getAllBlogPosts(int pageNo, int pageSize) {
        List<BlogPostResponse> response = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo, pageSize,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findAll(pageable);

        if (!pageResult.hasContent()) {
            throw new EmptyException(BlogPostErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()) {
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost,
                    BlogPostResponse.class);
            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getStaff().getId());
            }

            List<String> listImages = new ArrayList<String>();
            if (blogPost.getImages() != null && blogPost.getImages() != "") {
                String[] imageArray = blogPost.getImages().split("\n,");
                for (String image : imageArray) {
                    listImages.add(image.trim());
                }
            }
            blogPostResponse.setListImages(listImages);
            blogPostResponse.setCreateAt(blogPost.getDateCreated());
            response.add(blogPostResponse);
        }
        return response;
    }

    @Override
    public List<BlogPostResponse> getAllActiveBlogPosts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<BlogPost> pageResult = blogPostRepository.findByStatus(Status.ACTIVATED,
                pageable);

        List<BlogPostResponse> response = new ArrayList<>();
        if (!pageResult.hasContent()) {
            throw new EmptyException(BlogPostErrorMessage.EMPTY_MESSAGE);
        }

        for (BlogPost blogPost : pageResult.getContent()) {
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost,
                    BlogPostResponse.class);

            if (blogPost.getStaff() != null) {
                blogPostResponse.setStaffId(blogPost.getStaff().getId());
            }

            List<String> listImages = new ArrayList<String>();
            if (blogPost.getImages() != null && blogPost.getImages() != "") {
                String[] imageArray = blogPost.getImages().split("\n,");
                for (String image : imageArray) {
                    listImages.add(image.trim());
                }
            }
            blogPostResponse.setListImages(listImages);
            blogPostResponse.setCreateAt(blogPost.getDateCreated());
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

        if (blogPost.getStaff() != null) {
            response.setStaffId(blogPost.getStaff().getId());
        }

        List<String> listImages = new ArrayList<String>();
        if (blogPost.getImages() != null && blogPost.getImages() != "") {
            String[] imageArray = blogPost.getImages().split("\n,");
            for (String image : imageArray) {
                listImages.add(image.trim());
            }
        }
        response.setListImages(listImages);
        response.setCreateAt(blogPost.getDateCreated());

        return response;
    }

    @Override
    public BlogPostResponse createBlogPost(CreateBlogDTO createDTO) {
        BlogPostResponse response = new BlogPostResponse();

        Staff staff = staffRepository.findById(createDTO.getStaff()).orElseThrow(
                () -> new ErrorException(StaffErrorMessage.NOT_FOUND));

        BlogPost blog = BlogPost.builder()
                .title(createDTO.getTitle())
                .content(createDTO.getContent())
                .dateCreated(Utils.formatVNDatetimeNow())
                .images(createDTO.getImages())
                .status(Status.ACTIVATED)
                .staff(staff)
                .build();

        BlogPost newBlog = blogPostRepository.save(blog);

        response = modelMapper.map(newBlog, BlogPostResponse.class);

        List<String> listImages = new ArrayList<String>();
        if (newBlog.getImages() != null && newBlog.getImages() != "") {
            String[] imageArray = newBlog.getImages().split("\n,");
            for (String image : imageArray) {
                listImages.add(image.trim());
            }
        }
        response.setListImages(listImages);
        response.setCreateAt(newBlog.getDateCreated());
        response.setStaffId(staff.getId());
        return response;
    }

    @Override
    public BlogPostResponse updateBlogPost(UpdateBlogDTO updateDTO) {
        BlogPostResponse response = new BlogPostResponse();

        BlogPost blogPost = blogPostRepository.findById(updateDTO.getId()).orElseThrow(
                () -> new ErrorException(BlogPostErrorMessage.NOT_FOUND));

        blogPost.setTitle(updateDTO.getTitle());
        blogPost.setContent(updateDTO.getContent());
        blogPost.setImages(updateDTO.getImages());

        blogPostRepository.save(blogPost);

        response = modelMapper.map(blogPost, BlogPostResponse.class);
        response.setStaffId(blogPost.getStaff().getId());

        List<String> listImages = new ArrayList<String>();
        if (blogPost.getImages() != null && blogPost.getImages() != "") {
            String[] imageArray = blogPost.getImages().split("\n,");
            for (String image : imageArray) {
                listImages.add(image.trim());
            }
        }
        response.setListImages(listImages);
        response.setCreateAt(blogPost.getDateCreated());

        return response;
    }

    @Override
    public boolean deleteBlogPost(String id) {
        BlogPost blogPost = blogPostRepository.findById(id).orElseThrow(
                () -> new ErrorException(BlogPostErrorMessage.NOT_FOUND));

        blogPost.setStatus(Status.DISABLED);

        blogPostRepository.save(blogPost);

        return true;
    }
}
