package com.fu.weddingplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fu.weddingplatform.constant.account.AccountSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.constant.verifyToken.VerifyTokenSuccessMessage;
import com.fu.weddingplatform.request.Auth.LoginDTO;
import com.fu.weddingplatform.request.Auth.RegisterAdminDTO;
import com.fu.weddingplatform.request.Auth.RegisterCoupleDTO;
import com.fu.weddingplatform.request.Auth.RegisterStaffDTO;
import com.fu.weddingplatform.request.Auth.RegisterSupplierDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.Account.AccountResponse;
import com.fu.weddingplatform.response.Auth.LoginResponse;
import com.fu.weddingplatform.response.Auth.RegsiterCoupleReponse;
import com.fu.weddingplatform.response.Auth.RegsiterStaffReponse;
import com.fu.weddingplatform.response.Auth.RegsiterSupplierReponse;
import com.fu.weddingplatform.service.AuthService;
import com.fu.weddingplatform.service.VerificationTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

  @Autowired
  private final AuthService authService;

  @Autowired
  VerificationTokenService verificationTokenService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@Validated @RequestBody LoginDTO login) {
    ResponseDTO<LoginResponse> responseDTO = new ResponseDTO<LoginResponse>();
    LoginResponse loginResponseDTO = authService.login(login);
    responseDTO.setData(loginResponseDTO);
    responseDTO.setMessage("Login success");
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/loginGoogle")
  public ResponseEntity<?> loginGoogle(@RequestBody String token) {
    ResponseDTO<LoginResponse> responseDTO = new ResponseDTO<>();
    LoginResponse loginResponseDTO = authService.loginWithGoogle(token);
    responseDTO.setData(loginResponseDTO);
    responseDTO.setMessage("Login success");
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/registerNewAdminByAdmin")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
  public ResponseEntity<?> registerNewAdmin(@Validated @RequestBody RegisterAdminDTO registerDTO) {
    ResponseDTO<AccountResponse> responseDTO = new ResponseDTO<AccountResponse>();
    AccountResponse accountResponse = authService.registerNewAdmin(registerDTO);
    responseDTO.setData(accountResponse);
    responseDTO.setMessage(AccountSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/register/couple")
  public ResponseEntity<?> registerCouple(@Validated @RequestBody RegisterCoupleDTO registerDTO) {
    ResponseDTO<RegsiterCoupleReponse> responseDTO = new ResponseDTO<>();
    RegsiterCoupleReponse regsiterCoupleReponse = authService.registerCouple(registerDTO);
    responseDTO.setData(regsiterCoupleReponse);
    responseDTO.setMessage(AccountSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/register/staff")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
  public ResponseEntity<?> registerStaff(@Validated @RequestBody RegisterStaffDTO registerDTO) {
    ResponseDTO<RegsiterStaffReponse> responseDTO = new ResponseDTO<>();
    RegsiterStaffReponse registerStaffResponse = authService.registerStaff(registerDTO);
    responseDTO.setData(registerStaffResponse);
    responseDTO.setMessage(AccountSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/register/supplier")
  public ResponseEntity<?> registerSupplier(@Validated @RequestBody RegisterSupplierDTO registerDTO) {
    ResponseDTO<RegsiterSupplierReponse> responseDTO = new ResponseDTO<>();
    RegsiterSupplierReponse registerServiceSupplier = authService.registerSupplier(registerDTO);
    responseDTO.setData(registerServiceSupplier);
    responseDTO.setMessage(AccountSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/verify")
  public ModelAndView verifyEmail(@RequestParam("token") String token) {
      verificationTokenService.verifyAccount(token);
    return new ModelAndView("redirect:http://localhost:3000/login");
  }

  @PostMapping("/check/email")
  public ResponseEntity<?> checkEmailExist(@Validated @RequestParam String email) {
    ResponseDTO<Boolean> responseDTO = new ResponseDTO<>();
    Boolean registerServiceSupplier = authService.checkEmailExist(email);
    responseDTO.setData(registerServiceSupplier);
    responseDTO.setMessage(AccountSuccessMessage.CHECK_EXIST);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }
}
