package com.fu.weddingplatform.serviceImp;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.account.AccountErrorMessage;
import com.fu.weddingplatform.constant.verifyToken.VerifyTokenErrorMessage;
import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.entity.VerificationToken;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.AccountRepository;
import com.fu.weddingplatform.repository.VerifycationTokenRepository;
import com.fu.weddingplatform.request.email.MailVerifyAccountDTO;
import com.fu.weddingplatform.response.Account.AccountResponse;
import com.fu.weddingplatform.service.SentEmailService;
import com.fu.weddingplatform.service.VerificationTokenService;

@Service
public class VerificationTokenServiceImp implements VerificationTokenService {

  @Autowired
  VerifycationTokenRepository verifycationTokenRepository;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  SentEmailService sentEmailService;

  @Override
  public VerificationToken generateToken(int accountId) {

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));

    String token = UUID.randomUUID().toString();
    VerificationToken verificationToken = new VerificationToken();
    verificationToken.setToken(token);
    verificationToken.setAccount(account);
    verifycationTokenRepository.save(verificationToken);

    String link = "https://thedaywedding-hkaybdgafndhecbn.southeastasia-01.azurewebsites.net/auth/verify?token="
        + token;

    MailVerifyAccountDTO mailVerifyAccountDTO = MailVerifyAccountDTO.builder()
        .name(account.getName())
        .email(account.getEmail())
        .link(link)
        .build();

    sentEmailService.sentVerifyAccount(mailVerifyAccountDTO);
    return verificationToken;
  }

  @Override
  public AccountResponse verifyAccount(String token) {
    VerificationToken verificationToken = verifycationTokenRepository.findByToken(token)
        .orElseThrow(() -> new ErrorException(VerifyTokenErrorMessage.INVALID));

    Account account = verificationToken.getAccount();

    account.setEnabled(true);

    accountRepository.saveAndFlush(account);

    AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
    accountResponse.setRoleName(account.getRole().getName());
    return accountResponse;
  }

}
