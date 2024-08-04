package com.fu.weddingplatform.service;

import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.request.Auth.LoginDTO;
import com.fu.weddingplatform.request.Auth.RegisterAdminDTO;
import com.fu.weddingplatform.request.Auth.RegisterCoupleDTO;
import com.fu.weddingplatform.request.Auth.RegisterStaffDTO;
import com.fu.weddingplatform.request.Auth.RegisterSupplierDTO;
import com.fu.weddingplatform.response.Account.AccountResponse;
import com.fu.weddingplatform.response.Auth.LoginResponse;
import com.fu.weddingplatform.response.Auth.RegsiterCoupleReponse;
import com.fu.weddingplatform.response.Auth.RegsiterServiceSupplierReponse;
import com.fu.weddingplatform.response.Auth.RegsiterStaffReponse;

public interface AuthService {

    public LoginResponse login(LoginDTO loginDTO);

    public AccountResponse registerNewAdmin(RegisterAdminDTO registerDTO);

    public RegsiterCoupleReponse registerCouple(RegisterCoupleDTO registerDTO);

    public RegsiterStaffReponse registerStaff(RegisterStaffDTO registerDTO);

    public RegsiterServiceSupplierReponse registerServiceSupplier(RegisterSupplierDTO registerDTO);

    public LoginResponse loginWithGoogle(String token);

    public Account registerForGoogleLogin(String email, String name, String roleName);
}
