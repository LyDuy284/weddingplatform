package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.request.comment.CreateCommentDTO;
import com.fu.weddingplatform.request.comment.CreateReplyCommentDTO;
import com.fu.weddingplatform.response.comment.CommentResponse;
import com.fu.weddingplatform.response.comment.ReplyCommentResponse;

public interface CommentService {

  public CommentResponse createComment(CreateCommentDTO createDTO);

  public ReplyCommentResponse createReplyComment(CreateReplyCommentDTO createDTO);

  public CommentResponse getCommentById(String id);

  public List<CommentResponse> getAllCommentByBlogPost(String blogPostId, int pageNo, int pageSize, String sortBy,
      boolean isAscending);

}
