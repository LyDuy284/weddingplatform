package com.fu.weddingplatform.request.comment;

import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CreateCommentDTO {

  @NotEmpty(message = "Content " + ValidationMessage.NOT_EMPTY)
  private String content;
  @NotEmpty(message = "Blog Post ID " + ValidationMessage.NOT_EMPTY)
  private String blogId;
  @NotEmpty(message = "Couple ID " + ValidationMessage.NOT_EMPTY)
  private String coupleId;

}
