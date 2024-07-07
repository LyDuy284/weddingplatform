package com.fu.weddingplatform.response.serviceSupplier;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSupplierResponse {
    private String id;
    private String supplierName;
    private String contactPersonName;
    private String contactPhone;
    private String contactEmail;
    private String status;
}
