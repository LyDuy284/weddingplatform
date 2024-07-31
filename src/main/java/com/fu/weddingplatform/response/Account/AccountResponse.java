package com.fu.weddingplatform.response.Account;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private int id;
    private String roleName;
    private String email;
    private String image;
    private String name;
    private String phoneNumber;
    private String address;
    private String status;
}
