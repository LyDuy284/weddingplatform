package com.fu.weddingplatform.response.serviceSupplier;

import java.util.List;

import com.fu.weddingplatform.response.Account.SupplierResponse;
import com.fu.weddingplatform.response.service.ServiceResponse;

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
public class ServiceSupplierResponse {
    private String id;
    private String name;
    private String description;
    private float rating;
    private List<String> listImages;
    private String type;
    private String createAt;
    private int price;
    private String status;
    private SupplierResponse supplierResponse;
    private ServiceResponse serviceResponse;
}
