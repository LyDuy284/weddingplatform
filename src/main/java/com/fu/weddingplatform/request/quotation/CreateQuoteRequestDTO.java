package com.fu.weddingplatform.request.quotation;

import java.sql.Date;

import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateQuoteRequestDTO {
  @NotEmpty(message = "Couple ID " + ValidationMessage.NOT_EMPTY)
  private String coupleId;
  @NotEmpty(message = "Service ID " + ValidationMessage.NOT_EMPTY)
  private String serviceId;
  @NotEmpty(message = "Service Supplier ID " + ValidationMessage.NOT_EMPTY)
  private String supplierId;
  private String message;
  private Date eventDate;
}
