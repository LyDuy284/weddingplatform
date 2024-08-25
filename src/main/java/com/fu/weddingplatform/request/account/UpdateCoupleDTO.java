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
public class UpdateCoupleDTO {
  private String coupleId;
  private String name;
  private String image;
  private String phoneNumber;
  private String address;
  private String weddingDate;
  private String partnerName1;
  private String partnerName2;
}
