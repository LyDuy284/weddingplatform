package com.fu.weddingplatform.response.Login;

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
  private String roleName;
  private String email;
  private String status;

}
