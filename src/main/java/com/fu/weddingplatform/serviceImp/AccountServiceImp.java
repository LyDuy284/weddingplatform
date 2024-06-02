package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.Account.AccountErrorMessage;
import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.AccountRepository;
import com.fu.weddingplatform.response.Account.AccountResponse;
import com.fu.weddingplatform.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountServiceImp implements AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<AccountResponse> getAllUsersByAdmin(int pageNo, int pageSize) {
        List<AccountResponse> response = new ArrayList<AccountResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Account> pageResult = accountRepository.findAll(pageable);

        if (pageResult.hasContent()){
            for(Account account : pageResult.getContent()){
                AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
                accountResponse.setRoleName(account.getRole().getName());
                response.add(accountResponse);
            }
        } else {
            throw new ErrorException(AccountErrorMessage.EMPTY_ACCOUNT);
        }
        return response;
    }

    @Override
    public List<AccountResponse> getAllActivateUsersByAdmin(int pageNo, int pageSize) {
        List<AccountResponse> response = new ArrayList<AccountResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Account> pageResult = accountRepository.findByStatus(Status.ACTIVATED,pageable);

        if (pageResult.hasContent()){
            for(Account account : pageResult.getContent()){
                AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
                accountResponse.setRoleName(account.getRole().getName());
                response.add(accountResponse);
            }
        } else {
            throw new ErrorException(AccountErrorMessage.EMPTY_ACCOUNT);
        }
        return response;
    }
}
