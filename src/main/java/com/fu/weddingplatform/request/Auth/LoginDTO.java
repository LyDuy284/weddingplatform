package com.fu.weddingplatform.request.Auth;

import com.fu.weddingplatform.constant.validation.ValidationMessage;
import com.fu.weddingplatform.constant.validation.ValidationSize;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

  @Email(message = ValidationMessage.EMAIL_INVALID_MESSAGE)
  private String email;
  @Size(min = ValidationSize.PASSWORD_MIN, max = ValidationSize.PASSWORD_MAX,
          message = ValidationMessage.PASSWORD_INVALID_MESSAGE)
  private String password;

}
