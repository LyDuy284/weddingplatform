package com.fu.weddingplatform.request.email;

import com.fu.weddingplatform.entity.BookingDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelBookingMailForSupplierDTO {
  private String supplierName;
  private String supplierEmail;
  private String coupleName;
  private String phoneNumber;
  private BookingDetail bookingDetail;
  private String reason;
}
