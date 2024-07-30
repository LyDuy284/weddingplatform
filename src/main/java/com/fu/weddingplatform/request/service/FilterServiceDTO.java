package com.fu.weddingplatform.request.service;

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
public class FilterServiceDTO {
  @NotEmpty(message = "Category ID " + ValidationMessage.NOT_EMPTY)
  private String categoryId;
  private String type;
  private int minPrice;
  private int maxPrice;

}
