package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.request.Auth.RegisterCoupleDTO;

import com.fu.weddingplatform.request.Auth.RegisterServiceSupplierDTO;
import com.fu.weddingplatform.request.Auth.RegisterStaffDTO;
import com.fu.weddingplatform.response.Auth.RegsiterServiceSupplierReponse;
import com.fu.weddingplatform.response.Auth.RegsiterStaffReponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.request.Auth.LoginDTO;
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

  @PostMapping("/register/cupple")
  public ResponseEntity<ResponseDTO> registerCupple(@Validated @RequestBody RegisterCoupleDTO registerDTO) {
    ResponseDTO<RegsiterCoupleReponse> responseDTO = new ResponseDTO();
    RegsiterCoupleReponse regsiterCoupleReponse = authService.registerCouple(registerDTO);
    responseDTO.setData(regsiterCoupleReponse);
    responseDTO.setMessage("Register success");
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/register/staff")
  public ResponseEntity<ResponseDTO> registerStaff(@Validated @RequestBody RegisterStaffDTO registerDTO) {
    ResponseDTO<RegsiterStaffReponse> responseDTO = new ResponseDTO<>();
    RegsiterStaffReponse registerStaffResponse = authService.registerStaff(registerDTO);
    responseDTO.setData(registerStaffResponse);
    responseDTO.setMessage("Register success");
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/register/serviceSupplier")
  public ResponseEntity<ResponseDTO> registerServiceSupplier(@Validated @RequestBody RegisterServiceSupplierDTO registerDTO) {
    ResponseDTO<RegsiterServiceSupplierReponse> responseDTO = new ResponseDTO();
    RegsiterServiceSupplierReponse registerServiceSupplier = authService.registerServiceSupplier(registerDTO);
    responseDTO.setData(registerServiceSupplier);
    responseDTO.setMessage("Register success");
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }
}
