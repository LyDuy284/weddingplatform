package com.fu.weddingplatform.exception.handle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.exception.UsernameOrPasswordNotFoundException;

import java.util.HashMap;
import java.util.Map;

import com.fu.weddingplatform.constant.Account.AccountErrorMessage;
import com.fu.weddingplatform.response.ResponseDTO;

@RestControllerAdvice
public class ExceptionHandlers extends RuntimeException {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  @ExceptionHandler(value = { UsernameOrPasswordNotFoundException.class, AuthenticationException.class })
  public ResponseEntity<Object> usernameOrPasswordNotFound(AuthenticationException exception) {
    ResponseDTO dto = new ResponseDTO();
    dto.setMessage(AccountErrorMessage.INVALID_USERNAME_PASSWORD);
    dto.setStatus(ResponseStatusDTO.FAILURE);
    return ResponseEntity.badRequest().body(dto);
  }

  @ExceptionHandler(value = { ErrorException.class })
  public ResponseEntity<Object> ErrorException(ErrorException exception) {
    ResponseDTO dto = new ResponseDTO();
    dto.setMessage(exception.getMessage());
    dto.setStatus(ResponseStatusDTO.FAILURE);
    return ResponseEntity.badRequest().body(dto);
  }
}
