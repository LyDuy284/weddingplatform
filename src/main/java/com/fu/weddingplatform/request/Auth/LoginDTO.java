package com.fu.weddingplatform.request.Auth;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

  // @Email(message = ValidationMessage.EMAIL_VALID_MESSAGE_WHEN_LOGIN)
  private String email;
  // @Size(min = ValidationSize.PASSWORD_MIN, max = ValidationSize.PASSWORD_MAX,
  // message = ValidationMessage.PASSWORD_VALID_MESSAGE)
  private String password;

}
