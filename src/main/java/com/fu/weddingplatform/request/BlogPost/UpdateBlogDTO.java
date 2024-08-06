package com.fu.weddingplatform.request.blogPost;

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
public class UpdateBlogDTO {
  @NotEmpty(message = "Blog ID " + ValidationMessage.NOT_EMPTY)
  private String id;
  private String title;
  private String content;
  private String images;
}
