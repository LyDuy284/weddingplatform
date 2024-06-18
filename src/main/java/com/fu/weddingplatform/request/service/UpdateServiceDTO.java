package com.fu.weddingplatform.request.service;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceDTO {
    private String id;
    private String categoryId;
    private String name;
    private String description;
    private float price;
}
