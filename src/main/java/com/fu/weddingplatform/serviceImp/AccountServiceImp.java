package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.account.AccountErrorMessage;
import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.role.RoleErrorMessage;
import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.entity.Role;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.AccountRepository;
import com.fu.weddingplatform.repository.RoleRepository;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountServiceImp implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
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

    @Override
    public List<AccountResponse> getAllAccountByRole(int pageNo, int pageSize, String roleName) {
        List<AccountResponse> response = new ArrayList<AccountResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        Page<Account> pageResult = accountRepository.findByRole(role, pageable);

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
    public AccountResponse updateAccountStatus(int id, String status) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));

        account.setStatus(status);
        Account accountSaved = accountRepository.save(account);

        AccountResponse response = modelMapper.map(accountSaved, AccountResponse.class);
        response.setRoleName(account.getRole().getName());
        return response;
    }
}
