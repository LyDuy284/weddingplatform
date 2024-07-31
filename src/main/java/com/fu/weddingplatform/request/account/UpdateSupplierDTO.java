package com.fu.weddingplatform.request.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSupplierDTO {
  private String supplierId;
  private String name;
  private String image;
  private String phoneNumber;
  private String contactNumber;
  private String contactEmail;
  private String province;
  private String district;
  private String ward;
  private String apartmentNumber;
}
