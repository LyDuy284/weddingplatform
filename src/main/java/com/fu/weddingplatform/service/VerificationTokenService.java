package com.fu.weddingplatform.service;

import com.fu.weddingplatform.entity.VerificationToken;
import com.fu.weddingplatform.response.Account.AccountResponse;

public interface VerificationTokenService {
  public VerificationToken generateToken(int accountId);

  public AccountResponse verifyAccount(String token);
}