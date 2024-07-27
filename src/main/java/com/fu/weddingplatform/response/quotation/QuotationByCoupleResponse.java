package com.fu.weddingplatform.response.quotation;

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
public class QuotationByCoupleResponse {
  private String id;
  private String serviceSupplierId;
  private String serviceId;
  private float price;
  private String message;
  private Date eventDate;
  private String createAt;
  private String status;
}
