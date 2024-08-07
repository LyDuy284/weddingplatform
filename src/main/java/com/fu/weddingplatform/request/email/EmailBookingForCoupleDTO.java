package com.fu.weddingplatform.request.email;

import java.util.List;

import com.fu.weddingplatform.entity.BookingDetail;

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
public class EmailBookingForCoupleDTO {
  private String name;
  private String email;
  private String bookingId;
  private String createdAt;
  private List<BookingDetail> listBookingDetails;
  private String totalPrice;
}
