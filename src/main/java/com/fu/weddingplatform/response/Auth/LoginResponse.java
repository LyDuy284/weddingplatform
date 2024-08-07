package com.fu.weddingplatform.response.Auth;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
  private int accountId;
  private String token;
  private int balance;
  private String roleName;
  private String email;
  private String status;
  private String userId;

}
