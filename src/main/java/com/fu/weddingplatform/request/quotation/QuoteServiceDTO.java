package com.fu.weddingplatform.request.quotation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

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
public class QuoteServiceDTO {
  @NotEmpty(message = "Quotation ID " + ValidationMessage.NOT_EMPTY)
  private String quotationId;
  @Min(value = 0, message = "Value must be greater than 0")
  private int price;
}
