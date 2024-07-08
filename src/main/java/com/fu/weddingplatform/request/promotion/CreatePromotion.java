package com.fu.weddingplatform.request.promotion;

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
public class CreatePromotion {
  private String promotionDetails;
  private int percent;
  private Date startDate;
  private Date endDate;
  private String status;
}
