package com.fu.weddingplatform.response.serviceSupplier;

import java.util.List;

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
public class ServiceSupplierBySupplierReponse {
  private String categoryId;
  private String categoryName;
  private List<ServiceBaseOnCategory> listServices;

}
