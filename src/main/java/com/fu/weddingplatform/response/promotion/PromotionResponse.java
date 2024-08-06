package com.fu.weddingplatform.response.promotion;

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
public class PromotionResponse {
  private String id;
  private String name;
  private String type;
  private int value;
  private Date startDate;
  private Date endDate;
  private String status;
}
