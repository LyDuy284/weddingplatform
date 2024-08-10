package com.fu.weddingplatform.request.booking;

import java.sql.Date;

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
public class ServiceSupplierBookingDTO {
  private String serviceSupplierId;
  private int quantity;
  private Date dateCompleted;
  private String note;
}
