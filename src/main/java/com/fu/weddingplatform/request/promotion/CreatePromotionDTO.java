package com.fu.weddingplatform.request.promotion;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

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
public class CreatePromotionDTO {
  @NotEmpty(message = "Supplier ID " + ValidationMessage.NOT_EMPTY)
  private String supplierId;
  @Min(value = 0, message = "Value must be greater than 0")
  private int value;
  @NotEmpty(message = "Type " + ValidationMessage.NOT_EMPTY)
  private String type;
  private String name;
  private Date startDate;
  private Date endDate;
  private List<String> listServiceSupplierId;
}
