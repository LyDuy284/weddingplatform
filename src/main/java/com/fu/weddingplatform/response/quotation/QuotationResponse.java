package com.fu.weddingplatform.response.quotation;

import java.util.List;

import com.fu.weddingplatform.response.quoteResquest.QuoteResquestResponse;

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
public class QuotationResponse {
  private String id;
  private int price;
  private String status;
  private String serviceSupplierId;
  private String coupleId;
  private String bookingId;
  private String serviceId;
  private List<QuoteResquestResponse> quoteRequest;
}
