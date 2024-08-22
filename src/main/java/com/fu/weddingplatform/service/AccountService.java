package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.request.account.UpdateCoupleDTO;
import com.fu.weddingplatform.request.account.UpdateSupplierDTO;
import com.fu.weddingplatform.response.Account.AccountResponse;
import com.fu.weddingplatform.response.Account.SupplierResponse;
import com.fu.weddingplatform.response.couple.CoupleResponse;

public interface AccountService {
    public List<AccountResponse> getAllUsersByAdmin(int pageNo, int pageSize);

    public List<AccountResponse> getAllActivateUsersByAdmin(int pageNo, int pageSize);

    public List<AccountResponse> getAllAccountByRole(int pageNo, int pageSize, String roleName);

    public AccountResponse updateAccountStatus(int id, String status);

    public SupplierResponse updateSupplierProfile(UpdateSupplierDTO updateDTO);

    public CoupleResponse updateCoupleProfile(UpdateCoupleDTO updateCoupleDTO);

    public CoupleResponse getCoupleProfile(String id);

    public SupplierResponse getSupplierProfile(String id);

}
