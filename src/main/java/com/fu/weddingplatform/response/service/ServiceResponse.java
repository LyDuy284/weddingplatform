package com.fu.weddingplatform.response.service;

import java.util.List;

import com.fu.weddingplatform.response.category.CategoryResponse;
import com.fu.weddingplatform.response.promotion.PromotionByServiceResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private String type;
    private float price;
    private List<String> listImages;
    private String status;
    private CategoryResponse categoryResponse;
    private ServiceSupplierResponse serviceSupplierResponse;
    private List<PromotionByServiceResponse> promotions;
}
