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
public class ServiceSupplierBaseOnService {
  private String id;
  private String name;
  private String description;
  private List<String> listImages;
  private String type;
  private String createAt;
  private int price;
  private String status;

}
