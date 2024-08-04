package com.fu.weddingplatform.response.booking;

import com.fu.weddingplatform.response.promotion.PromotionResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookingDetailResponse {
  private ServiceSupplierResponse serviceSupplierResponse;
  private String id;
  private int price;
  private String note;
  private String completedDate;
  private String status;
  private PromotionResponse promotionResponse;
}
