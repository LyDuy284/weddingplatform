package com.fu.weddingplatform.serviceImp;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.crypto.SecretKey;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.role.RoleErrorMessage;
import com.fu.weddingplatform.constant.role.RoleName;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Role;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.request.Auth.RegisterCoupleDTO;
import com.fu.weddingplatform.response.Login.RegsiterCoupleReponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Account.AccountErrorMessage;
import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.jwt.JwtConfig;
import com.fu.weddingplatform.repository.AccountRepository;
import com.fu.weddingplatform.repository.RoleRepository;
import com.fu.weddingplatform.request.Auth.LoginDTO;
import com.fu.weddingplatform.response.Login.LoginResponse;
import com.fu.weddingplatform.service.AuthService;
import com.fu.weddingplatform.utils.Utils;

@Service
@AllArgsConstructor
public class AuthServiceImp implements AuthService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CoupleRepository coupleRepository;
    private final ModelMapper modelMapper;

    @Override
    public LoginResponse login(LoginDTO loginDTO) {
        Account account = accountRepository.findAccountByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
                loginDTO.getPassword());
        LoginResponse loginResponse = null;
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            Optional<Account> accountAuthencatedOptional = accountRepository.findAccountByEmail(loginDTO.getEmail());
            Account accountAuthencated = accountAuthencatedOptional.get();
            String token = Utils.buildJWT(authenticate, accountAuthencated, secretKey, jwtConfig);
             loginResponse = LoginResponse.builder()
                     .accountId(accountAuthencated.getId())
                     .email(accountAuthencated.getEmail())
                     .status(accountAuthencated.getStatus())
                     .roleName(accountAuthencated.getRole().getName())
                     .token(token)
                     .build();

        }
        return loginResponse;
    }

    @Override
    public RegsiterCoupleReponse registerCouple(RegisterCoupleDTO registerDTO) {
        Optional<Account> optionalUser = accountRepository.findAccountByEmail(registerDTO.getEmail());

        Role role = roleRepository.findByName(RoleName.ROLE_COUPLE)
                .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        RegsiterCoupleReponse response = new RegsiterCoupleReponse();

        if(optionalUser.isPresent()) {
            throw new ErrorException(AccountErrorMessage.EXIST_EMAIL);
        }

        Account account = new Account().builder()
                .name(registerDTO.getName())
                .address(registerDTO.getAddress())
                .email(registerDTO.getEmail())
                .phoneNumber(registerDTO.getPhoneNumber())
                .role(role)
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .status(Status.ACTIVATED)
                .build();
        Account newAccount = accountRepository.save(account);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate weddingDate = LocalDate.parse(registerDTO.getWeddingDate().toString(), dateFormatter);

        Couple couple = Couple.builder()
                .partnerName1(registerDTO.getPartnerName1())
                .partnerName2(registerDTO.getPartnerName2())
                .weddingDate(Date.valueOf(weddingDate))
                .status(Status.STARTED)
                .account(newAccount)
                .build();

        Couple newCouple = coupleRepository.save(couple);

        response = modelMapper.map(newAccount, RegsiterCoupleReponse.class);

        response.setAccountId(newAccount.getId());
        response.setRoleName(role.getName());
        response.setPartnerName1(couple.getPartnerName1());
        response.setPartnerName2(couple.getPartnerName2());
        response.setWeddingDate(newCouple.getWeddingDate().toString());
        response.setCoupleId(newCouple.getId());
        return response;
    }

}
