package com.fu.weddingplatform.response.service;

import com.fu.weddingplatform.response.category.CategoryResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private String id;
    private String name;
    private String description;
    private float price;
    private String status;
    private CategoryResponse categoryResponse;
    private ServiceSupplierResponse serviceSupplierResponse;
}
