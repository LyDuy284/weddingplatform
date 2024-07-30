package com.fu.weddingplatform.response.service;

import java.util.List;

import com.fu.weddingplatform.response.promotion.PromotionByServiceResponse;

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
public class ServiceByCategoryAndSupplierResponse {
  private String id;
  private String name;
  private String description;
  private List<String> listImages;
  private float price;
  private String status;
  private List<PromotionByServiceResponse> promotions;
}
