package com.fu.weddingplatform.response.Account;

import com.fu.weddingplatform.entity.Area;

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
public class SupplierResponse {
  private String supplierId;
  private String name;
  private String image;
  private String phoneNumber;
  private String contactNumber;
  private String contactEmail;
  private Area area;
}
