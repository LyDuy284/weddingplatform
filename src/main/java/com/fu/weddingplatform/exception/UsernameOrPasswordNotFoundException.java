package com.fu.weddingplatform.exception;

public class UsernameOrPasswordNotFoundException extends RuntimeException {
  public UsernameOrPasswordNotFoundException(String message) {
    super(message);
  }
}