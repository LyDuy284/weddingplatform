package com.fu.weddingplatform.request.blogPost;

import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBlogDTO {
    private String title;
    private String content;
    private String images;
    @NotEmpty(message = "Staff ID " + ValidationMessage.NOT_EMPTY)
    private String staff;
}
