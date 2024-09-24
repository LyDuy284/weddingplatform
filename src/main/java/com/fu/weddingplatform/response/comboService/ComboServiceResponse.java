package com.fu.weddingplatform.response.comboService;

import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComboServiceResponse {
    String id;
    String status;
    ServiceSupplierResponse serviceSupplier;
}
