package com.fu.weddingplatform.response.quoteResquest;

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
public class QuoteResquestResponse {
  private String id;
  private String message;
  private Date eventDate;
  private String status;
}
