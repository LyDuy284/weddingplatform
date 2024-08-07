package com.fu.weddingplatform.request.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailCreateBookingToSupplier {
  private String email;
  private String name;
  private String bookingDetailId;
  private String createAt;
  private String customerName;
  private String phone;
  private String serviceSupplierName;
  private String price;
  private String completeDate;
  private String note;

}
