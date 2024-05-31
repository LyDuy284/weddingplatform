package com.fu.weddingplatform.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ResponseDTO<T> {
  private String message;
  private T data;
  private String status;
}