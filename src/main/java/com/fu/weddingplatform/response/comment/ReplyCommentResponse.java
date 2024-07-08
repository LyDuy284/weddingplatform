package com.fu.weddingplatform.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyCommentResponse {
  private String replyCommentId;
  private String coupleId;
  private String blogPostId;
  private String parentCommentId;
  private String content;
  private String createdAt;
  private String status;
}
