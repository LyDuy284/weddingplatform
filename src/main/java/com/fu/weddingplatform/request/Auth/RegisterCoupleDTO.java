package com.fu.weddingplatform.request.Auth;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterCoupleDTO {
    private String password;
    private String email;
    private String name;
    private String roleName;
    private String phoneNumber;
    private String address;
    private String partnerName1;
    private String partnerName2;
    private Date weddingDate;

}
