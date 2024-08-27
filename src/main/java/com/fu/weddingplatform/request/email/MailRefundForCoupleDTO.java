package com.fu.weddingplatform.request.email;

import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.Couple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MailRefundForCoupleDTO {
  private BookingDetail bookingDetail;
  private Couple couple;
  private String supplierName;
  private String totalAmount;
  private String platformAmount;
  private String supplierAmount;
  private String receivedAmount;
}
