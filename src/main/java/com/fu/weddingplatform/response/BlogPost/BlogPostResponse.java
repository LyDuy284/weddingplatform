package com.fu.weddingplatform.response.BlogPost;

import java.util.List;

import com.fu.weddingplatform.response.comment.CommentResponse;

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
public class BlogPostResponse {

    private String id;
    private String title;
    private String content;
    private String createAt;
    private List<String> listImages;
    private List<CommentResponse> listComments;
    private String serviceSupplierId;
    private String staffId;
    private String status;

}
