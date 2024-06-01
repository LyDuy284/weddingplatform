package com.fu.weddingplatform.service;

import com.fu.weddingplatform.request.Auth.LoginDTO;
import com.fu.weddingplatform.request.Auth.RegisterCoupleDTO;
import com.fu.weddingplatform.request.Auth.RegisterServiceSupplierDTO;
import com.fu.weddingplatform.request.Auth.RegisterStaffDTO;
import com.fu.weddingplatform.response.Auth.LoginResponse;
import com.fu.weddingplatform.response.Auth.RegsiterCoupleReponse;
import com.fu.weddingplatform.response.Auth.RegsiterServiceSupplierReponse;
import com.fu.weddingplatform.response.Auth.RegsiterStaffReponse;

public interface AuthService {

    public LoginResponse login(LoginDTO loginDTO);

    public RegsiterCoupleReponse registerCouple(RegisterCoupleDTO registerDTO);

    public RegsiterStaffReponse registerStaff(RegisterStaffDTO registerDTO);

    public RegsiterServiceSupplierReponse registerServiceSupplier(RegisterServiceSupplierDTO registerDTO);

}
