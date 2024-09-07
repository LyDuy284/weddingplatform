package com.fu.weddingplatform.request.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailVerifyAccountDTO {
  private String email;
  private String link;
  private String name;

}
