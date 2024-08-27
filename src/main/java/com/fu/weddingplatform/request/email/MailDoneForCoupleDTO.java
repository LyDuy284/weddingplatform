package com.fu.weddingplatform.request.email;

import java.util.List;

import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.Couple;

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
public class MailDoneForCoupleDTO {
  private Couple couple;
  private BookingDetail bookingDetail;
  private List<BookingDetail> currentBooking;
  private String bookingDetailAmount;
  private String paymentBookingDetailAmount;
  private String remainingookingDetailAmount;
  private String totalAmount;
  private String paymentAmount;
  private String remaining;
}
