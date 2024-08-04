package com.fu.weddingplatform.request.booking;

import java.util.List;

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
public class CreateBookingDTO {
  private String coupleId;
  private List<ServiceSupplierBookingDTO> listServiceSupplier;
}
