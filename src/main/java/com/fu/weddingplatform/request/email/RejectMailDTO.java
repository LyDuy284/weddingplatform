package com.fu.weddingplatform.request.email;

import java.util.List;

import com.fu.weddingplatform.entity.BookingDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RejectMailDTO {
  private String mail;
  private String coupleName;
  private String supplierName;
  private String reason;
  private BookingDetail bookingDetail;
  private List<BookingDetail> listCurrentBookingDetails;
  private String totalPrice;
  private String paidPrice;
  private String remaining;
}
