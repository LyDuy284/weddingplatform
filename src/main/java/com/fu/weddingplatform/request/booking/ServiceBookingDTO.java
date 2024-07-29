package com.fu.weddingplatform.request.booking;

import javax.validation.constraints.Min;

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
public class ServiceBookingDTO {
  private String serviceId;
  @Min(value = 0, message = "Price must be greater than 0")
  private int price;
}
