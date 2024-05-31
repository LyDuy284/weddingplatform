package com.fu.weddingplatform.auth;

import java.util.Optional;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.repository.AccountRepository;

@Service
@Data
@RequiredArgsConstructor
@Builder
public class ApplicationUserService implements UserDetailsService {
    private final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email){
        Optional<Account> account = accountRepository.findAccountByEmail(email);
        if (!account.isPresent()) {
            throw new IllegalStateException("Employee Not Found");
        }
        return new UserDetail(account.get());
    }
}

