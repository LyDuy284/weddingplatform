package com.fu.weddingplatform.request.service;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceDTO {
    private String categoryId;
    private String serviceSupplierId;
    private String name;
    private String description;
    private float price;
}
