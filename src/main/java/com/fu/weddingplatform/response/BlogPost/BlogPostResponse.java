package com.fu.weddingplatform.response.BlogPost;

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
public class BlogPostResponse {

    private String id;
    private String title;
    private String content;
    private String createAt;
    private List<String> listImages;
    private String staffId;
    private String status;

}
