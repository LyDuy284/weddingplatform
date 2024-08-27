package com.fu.weddingplatform.request.email;

import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessingMailForCoupleDTO {
  private Couple couple;
  private BookingDetail bookingDetail;
  private Supplier supplier;
  private String totalPrice;
  private String paymentAmount;
  private String remainingAmount;
}
