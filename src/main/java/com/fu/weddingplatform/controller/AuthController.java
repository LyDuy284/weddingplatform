package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.request.Auth.RegisterCoupleDTO;
import com.fu.weddingplatform.response.Login.RegsiterCoupleReponse;
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
import com.fu.weddingplatform.response.Login.LoginResponse;
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

  @PostMapping("/register")
  public ResponseEntity<ResponseDTO> register(@Validated @RequestBody RegisterCoupleDTO registerDTO) {
    ResponseDTO<RegsiterCoupleReponse> responseDTO = new ResponseDTO();
    RegsiterCoupleReponse regsiterCoupleReponse = authService.registerCouple(registerDTO);
    responseDTO.setData(regsiterCoupleReponse);
    responseDTO.setMessage("Register success");
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }
}
