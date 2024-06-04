package com.fu.weddingplatform.response.Auth;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegsiterStaffReponse {
    private int accountId;
    private String roleName;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private String staffId;
    private String position;
    private String department;
    private String status;
}
