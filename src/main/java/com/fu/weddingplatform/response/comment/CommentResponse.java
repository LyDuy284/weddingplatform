package com.fu.weddingplatform.response.comment;

import java.util.List;

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
public class CommentResponse {
  private String id;
  private String coupleId;
  private String blogId;
  private String content;
  private String createdAt;
  private List<ReplyCommentResponse> replyCommentsResponse;
  private String status;
}
