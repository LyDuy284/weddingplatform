package com.fu.weddingplatform.response.booking;

import com.fu.weddingplatform.response.couple.CoupleResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingGroupBySupplierResponse {
  private String id;
  private String note;
  private int totalPrice;
  private String createdAt;
  private String status;
  private CoupleResponse coupleResponse;
}
