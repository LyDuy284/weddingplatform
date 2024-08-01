package com.fu.weddingplatform.response.booking;

import java.sql.Date;
import java.util.List;

import com.fu.weddingplatform.response.couple.CoupleResponse;

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
public class BookingResponse {
  private String id;
  private CoupleResponse couple;
  private String createdAt;
  private Date completedDate;
  private List<ServiceBookingResponse> serviceBookings;
  private int totalPrice;
  private String status;
}
