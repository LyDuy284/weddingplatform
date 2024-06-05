package com.fu.weddingplatform.request.BlogPost;

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
    private String serviceSupplierId;
}
