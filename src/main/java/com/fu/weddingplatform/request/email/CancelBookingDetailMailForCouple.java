package com.fu.weddingplatform.request.email;

import java.util.List;

import com.fu.weddingplatform.entity.Booking;
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
public class CancelBookingDetailMailForCouple {
  private String coupleName;
  private String coupleMail;
  private BookingDetail bookingDetail;
  private List<BookingDetail> currentBooking;
  private Booking booking;
  private String totalAmount;
  private String paymentAmount;
  private String remaining;
  private String reason;
}
