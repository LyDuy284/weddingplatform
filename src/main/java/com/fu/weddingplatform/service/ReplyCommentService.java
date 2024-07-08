package com.fu.weddingplatform.service;

import com.fu.weddingplatform.entity.Comment;
import com.fu.weddingplatform.request.replyComment.CreateReplyCommentDTO;

public interface ReplyCommentService {
  public Comment createReplyComment(CreateReplyCommentDTO createDTO);
}
