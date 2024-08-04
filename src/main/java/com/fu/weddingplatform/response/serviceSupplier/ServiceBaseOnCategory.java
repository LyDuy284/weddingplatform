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
public class ServiceBaseOnCategory {
  private String id;
  private String name;
  private String description;
  private String createAt;
  private List<String> listImages;
  private List<ServiceSupplierBaseOnService> listServiceSupplier;
}
