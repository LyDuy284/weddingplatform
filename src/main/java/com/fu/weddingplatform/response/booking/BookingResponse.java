package com.fu.weddingplatform.response.booking;

import java.sql.Date;
import java.util.List;

import com.fu.weddingplatform.request.booking.ServiceBookingDTO;

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
  private String coupleId;
  private String createdAt;
  private Date completedDate;
  private List<ServiceBookingDTO> serviceBookings;
  private int totalPrice;
  private String status;
}
