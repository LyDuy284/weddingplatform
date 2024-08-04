package com.fu.weddingplatform.response.serviceSupplier;

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
public class GroupByCategory {
  private String categoryId;
  private String serviceId;
  private String serviceSupplierId;
}
