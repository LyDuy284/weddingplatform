package com.fu.weddingplatform.request.comment;

import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

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
public class CreateReplyCommentDTO {
  @NotEmpty(message = "Content " + ValidationMessage.NOT_EMPTY)
  private String content;
  @NotEmpty(message = "Couple ID " + ValidationMessage.NOT_EMPTY)
  private String coupleId;
  @NotEmpty(message = "Comment ID " + ValidationMessage.NOT_EMPTY)
  private String parentCommentId;
}
