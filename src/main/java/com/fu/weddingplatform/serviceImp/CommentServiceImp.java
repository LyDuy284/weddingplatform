package com.fu.weddingplatform.serviceImp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.blogPost.BlogPostErrorMessage;
import com.fu.weddingplatform.constant.comment.CommentErrorMessage;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.entity.BlogPost;
import com.fu.weddingplatform.entity.Comment;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.exception.EmptyException;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BlogPostRepository;
import com.fu.weddingplatform.repository.CommentRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.request.comment.CreateCommentDTO;
import com.fu.weddingplatform.request.comment.CreateReplyCommentDTO;
import com.fu.weddingplatform.response.comment.CommentResponse;
import com.fu.weddingplatform.response.comment.ReplyCommentResponse;
import com.fu.weddingplatform.service.CommentService;

@Service
public class CommentServiceImp implements CommentService {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private BlogPostRepository blogPostRepository;

  @Autowired
  private CoupleRepository coupleRepository;

  @Override
  public CommentResponse createComment(CreateCommentDTO createDTO) {

    Couple couple = coupleRepository.findById(createDTO.getCoupleId())
        .orElseThrow(() -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

    BlogPost blogPost = blogPostRepository.findById(createDTO.getBlogId())
        .orElseThrow(() -> new ErrorException(BlogPostErrorMessage.NOT_FOUND));

    ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);

    Comment comment = new Comment().builder()
        .content(createDTO.getContent())
        .createdAt(localDateTime.format(dateTimeFormatter))
        .blogPost(blogPost)
        .status(Status.ACTIVATED)
        .couple(couple)
        .build();

    Comment commentSaved = commentRepository.save(comment);

    CommentResponse response = modelMapper.map(commentSaved, CommentResponse.class);
    response.setBlogId(blogPost.getId());
    response.setCoupleId(couple.getId());
    return response;
  }

  @Override
  public CommentResponse getCommentById(String id) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new ErrorException(CommentErrorMessage.NOT_FOUND));

    CommentResponse response = modelMapper.map(comment, CommentResponse.class);
    response.setBlogId(comment.getBlogPost().getId());
    response.setCoupleId(comment.getCouple().getId());

    List<ReplyCommentResponse> replyComments = new ArrayList<ReplyCommentResponse>();

    for (Comment replyComment : comment.getReplyComments()) {

      ReplyCommentResponse replyCommentResponse = modelMapper.map(replyComments, ReplyCommentResponse.class);
      replyCommentResponse.setReplyCommentId(replyComment.getId());
      replyCommentResponse.setBlogPostId(comment.getBlogPost().getId());
      replyCommentResponse.setCoupleId(replyComment.getCouple().getId());
      replyComments.add(replyCommentResponse);

    }

    response.setReplyCommentsResponse(replyComments);

    return response;
  }

  @Override
  public List<CommentResponse> getAllCommentByBlogPost(String blogPostId, int pageNo, int pageSize, String sortBy,
      boolean isAscending) {

    BlogPost blogPost = blogPostRepository.findById(blogPostId).orElseThrow(
        () -> new ErrorException(BlogPostErrorMessage.NOT_FOUND));

    Page<Comment> pageResult = commentRepository.findByParentIsNullAndBlogPost(blogPost,
        PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy)));

    List<CommentResponse> response = new ArrayList<>();

    if (!pageResult.hasContent()) {
      throw new EmptyException(BlogPostErrorMessage.EMPTY_MESSAGE);
    }

    for (Comment comment : pageResult.getContent()) {
      CommentResponse commentResponse = getCommentById(comment.getId());
      response.add(commentResponse);
    }

    return response;
  }

  @Override
  public ReplyCommentResponse createReplyComment(CreateReplyCommentDTO createDTO) {
    Couple couple = coupleRepository.findById(createDTO.getCoupleId())
        .orElseThrow(() -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

    Comment parent = commentRepository.findById(createDTO.getParentCommentId())
        .orElseThrow(() -> new ErrorException(CommentErrorMessage.NOT_FOUND));

    ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);

    Comment comment = new Comment().builder()
        .content(createDTO.getContent())
        .createdAt(localDateTime.format(dateTimeFormatter))
        .parent(parent)
        .blogPost(parent.getBlogPost())
        .status(Status.ACTIVATED)
        .couple(couple)
        .build();

    Comment commentSaved = commentRepository.save(comment);

    ReplyCommentResponse response = modelMapper.map(commentSaved, ReplyCommentResponse.class);
    response.setReplyCommentId(commentSaved.getId());
    response.setBlogPostId(parent.getBlogPost().getId());
    response.setCoupleId(couple.getId());
    return response;
  }

}
