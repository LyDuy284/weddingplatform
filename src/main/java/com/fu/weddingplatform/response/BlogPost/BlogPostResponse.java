package com.fu.weddingplatform.response.BlogPost;

import lombok.*;

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
    private String dateCreated;
    private String serviceSupplierId;
    private String staffId;
    private String status;

}
