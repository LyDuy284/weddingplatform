package com.fu.weddingplatform.response.Auth;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegsiterCoupleReponse {
    private int accountId;
    private String roleName;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private String coupleId;
    private String partnerName1;
    private String partnerName2;
    private String weddingDate;
    private String status;
}
