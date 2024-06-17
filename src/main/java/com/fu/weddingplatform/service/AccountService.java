package com.fu.weddingplatform.service;

import com.fu.weddingplatform.response.Account.AccountResponse;

import java.util.List;

public interface AccountService {
    public List<AccountResponse> getAllUsersByAdmin(int pageNo, int pageSize);

    public List<AccountResponse> getAllActivateUsersByAdmin(int pageNo, int pageSize);

    public List<AccountResponse> getAllAccountByRole(int pageNo, int pageSize, String roleName);

    public AccountResponse updateAccountStatus(int id, String status);
}
