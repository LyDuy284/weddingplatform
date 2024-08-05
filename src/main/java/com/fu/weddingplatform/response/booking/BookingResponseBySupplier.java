package com.fu.weddingplatform.response.booking;

import java.util.List;

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
public class BookingResponseBySupplier {
  private String id;
  private List<BookingDetailBySupplierResponse> listBookingDetail;
  private String note;
  private int totalPrice;
  private String createdAt;
  private String status;
}
