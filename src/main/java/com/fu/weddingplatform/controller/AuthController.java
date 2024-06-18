package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.account.AccountSuccessMessage;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;

import com.fu.weddingplatform.request.Auth.*;
import com.fu.weddingplatform.response.Account.AccountResponse;
import com.fu.weddingplatform.response.Auth.RegsiterServiceSupplierReponse;
import com.fu.weddingplatform.response.Auth.RegsiterStaffReponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.Auth.LoginResponse;
import com.fu.weddingplatform.response.Auth.RegsiterCoupleReponse;
import com.fu.weddingplatform.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ResponseDTO> login(@Validated @RequestBody LoginDTO login) {
    ResponseDTO<LoginResponse> responseDTO = new ResponseDTO<LoginResponse>();
    LoginResponse loginResponseDTO = authService.login(login);
    responseDTO.setData(loginResponseDTO);
    responseDTO.setMessage("Login success");
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/loginGoogle")
  public ResponseEntity<ResponseDTO> loginGoogle(@RequestBody String token) {
    ResponseDTO<LoginResponse> responseDTO = new ResponseDTO();
    LoginResponse loginResponseDTO = authService.loginWithGoogle(token);
    responseDTO.setData(loginResponseDTO);
    responseDTO.setMessage("Login success");
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/registerNewAdminByAdmin")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
  public ResponseEntity<ResponseDTO> registerNewAdmin(@Validated @RequestBody RegisterAdminDTO registerDTO) {
    ResponseDTO<AccountResponse> responseDTO = new ResponseDTO<AccountResponse>();
    AccountResponse accountResponse = authService.registerNewAdmin(registerDTO);
    responseDTO.setData(accountResponse);
    responseDTO.setMessage(AccountSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/register/couple")
  public ResponseEntity<ResponseDTO> registerCouple(@Validated @RequestBody RegisterCoupleDTO registerDTO) {
    ResponseDTO<RegsiterCoupleReponse> responseDTO = new ResponseDTO();
    RegsiterCoupleReponse regsiterCoupleReponse = authService.registerCouple(registerDTO);
    responseDTO.setData(regsiterCoupleReponse);
    responseDTO.setMessage(AccountSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/register/staff")
  public ResponseEntity<ResponseDTO> registerStaff(@Validated @RequestBody RegisterStaffDTO registerDTO) {
    ResponseDTO<RegsiterStaffReponse> responseDTO = new ResponseDTO<>();
    RegsiterStaffReponse registerStaffResponse = authService.registerStaff(registerDTO);
    responseDTO.setData(registerStaffResponse);
    responseDTO.setMessage(AccountSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/register/serviceSupplier")
  public ResponseEntity<ResponseDTO> registerServiceSupplier(@Validated @RequestBody RegisterServiceSupplierDTO registerDTO) {
    ResponseDTO<RegsiterServiceSupplierReponse> responseDTO = new ResponseDTO();
    RegsiterServiceSupplierReponse registerServiceSupplier = authService.registerServiceSupplier(registerDTO);
    responseDTO.setData(registerServiceSupplier);
    responseDTO.setMessage(AccountSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }
}
