package com.fu.weddingplatform.response.Auth;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegsiterSupplierReponse {
    private int accountId;
    private String roleName;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private String SupplierId;
    private String supplierName;
    private String contactEmail;
    private String contactPhone;
    private String contactPersonName;

}
