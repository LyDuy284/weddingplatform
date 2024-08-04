package com.fu.weddingplatform.response.supplier;

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
public class SupplierResponse {
    private String id;
    private String supplierName;
    private String contactPersonName;
    private String contactPhone;
    private String contactEmail;
    private String status;
}
