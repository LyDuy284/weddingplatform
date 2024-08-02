package com.fu.weddingplatform.response.booking;

import com.fu.weddingplatform.response.service.ServiceResponse;

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
public class ServiceBookingResponse {
  private ServiceResponse service;
  private String completedDate;
  private int bookingPrice;
  private int originalPrice;
  private String status;
}
