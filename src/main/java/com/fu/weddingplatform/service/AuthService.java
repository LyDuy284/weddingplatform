package com.fu.weddingplatform.service;

import com.fu.weddingplatform.request.Auth.LoginDTO;
import com.fu.weddingplatform.request.Auth.RegisterCoupleDTO;
import com.fu.weddingplatform.response.Login.LoginResponse;
import com.fu.weddingplatform.response.Login.RegsiterCoupleReponse;

public interface AuthService {

    public LoginResponse login(LoginDTO loginDTO);

    public RegsiterCoupleReponse registerCouple(RegisterCoupleDTO registerDTO);

}
