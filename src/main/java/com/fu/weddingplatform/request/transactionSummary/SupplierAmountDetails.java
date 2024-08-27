package com.fu.weddingplatform.request.transactionSummary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SupplierAmountDetails {
  private String supplierId;
  private String image;
  private String supplierName;
  private String contactPersonName;
  private String contactPhone;
  private String contactEmail;
  private String area;
  private int price;

}
