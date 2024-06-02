package com.fu.weddingplatform.serviceImp;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.crypto.SecretKey;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.role.RoleErrorMessage;
import com.fu.weddingplatform.constant.role.RoleName;
import com.fu.weddingplatform.entity.*;
import com.fu.weddingplatform.repository.*;
import com.fu.weddingplatform.request.Auth.*;
import com.fu.weddingplatform.response.Account.AccountResponse;
import com.fu.weddingplatform.response.Auth.LoginResponse;
import com.fu.weddingplatform.response.Auth.RegsiterCoupleReponse;

import com.fu.weddingplatform.response.Auth.RegsiterServiceSupplierReponse;
import com.fu.weddingplatform.response.Auth.RegsiterStaffReponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Account.AccountErrorMessage;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.jwt.JwtConfig;
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
    private final StaffRepository staffRepository;
    private final ServiceSupplierRepository serviceSupplierRepository;
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
    public AccountResponse registerNewAdmin(RegisterAdminDTO registerDTO) {
        Optional<Account> optionalUser = accountRepository.findAccountByEmail(registerDTO.getEmail());

        Role role = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        AccountResponse response = new AccountResponse();

        if(optionalUser.isPresent()) {
            throw new ErrorException(AccountErrorMessage.EXIST_EMAIL_ACCOUNT);
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

        response = modelMapper.map(newAccount, AccountResponse.class);
        response.setRoleName(role.getName());
        return response;
    }

    @Override
    public RegsiterCoupleReponse registerCouple(RegisterCoupleDTO registerDTO) {
        Optional<Account> optionalUser = accountRepository.findAccountByEmail(registerDTO.getEmail());

        Role role = roleRepository.findByName(RoleName.ROLE_COUPLE)
                .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        RegsiterCoupleReponse response = new RegsiterCoupleReponse();

        if(optionalUser.isPresent()) {
            throw new ErrorException(AccountErrorMessage.EXIST_EMAIL_ACCOUNT);
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

    @Override
    public RegsiterStaffReponse registerStaff(RegisterStaffDTO registerDTO) {
        Optional<Account> optionalUser = accountRepository.findAccountByEmail(registerDTO.getEmail());

        Role role = roleRepository.findByName(RoleName.ROLE_STAFF)
                .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        RegsiterStaffReponse response = new RegsiterStaffReponse();

        if(optionalUser.isPresent()) {
            throw new ErrorException(AccountErrorMessage.EXIST_EMAIL_ACCOUNT);
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

        Staff staff = new Staff().builder()
                .department(registerDTO.getDepartment())
                .position(registerDTO.getPosition())
                .account(newAccount)
                .status(Status.ACTIVATED)
                .build();


        Staff newStaff = staffRepository.save(staff);

        response = modelMapper.map(newAccount, RegsiterStaffReponse.class);

        response.setAccountId(newAccount.getId());
        response.setRoleName(role.getName());
        response.setDepartment(newStaff.getDepartment());
        response.setPosition(newStaff.getPosition());
        response.setStaffId(newStaff.getId());
        return response;
    }

    @Override
    public RegsiterServiceSupplierReponse registerServiceSupplier(RegisterServiceSupplierDTO registerDTO) {
        Optional<Account> optionalUser = accountRepository.findAccountByEmail(registerDTO.getEmail());

        Role role = roleRepository.findByName(RoleName.ROLE_SERVICE_SUPPLIER)
                .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        RegsiterServiceSupplierReponse response = new RegsiterServiceSupplierReponse();

        if(optionalUser.isPresent()) {
            throw new ErrorException(AccountErrorMessage.EXIST_EMAIL_ACCOUNT);
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

        ServiceSupplier serviceSupplier = new ServiceSupplier()
                .builder()
                .slot(registerDTO.getSlot())
                .supplierName(registerDTO.getSupplierName())
                .supplierAddress(registerDTO.getSupplierAddress())
                .contactPersonName(registerDTO.getContactPersonName())
                .contactPhone(registerDTO.getContactPhone())
                .contactEmail(registerDTO.getContactEmail())
                .status(Status.ACTIVATED)
                .build();


        ServiceSupplier newServiceSupplier = serviceSupplierRepository.save(serviceSupplier);

        response = modelMapper.map(newAccount, RegsiterServiceSupplierReponse.class);

        response.setAccountId(newAccount.getId());
        response.setRoleName(role.getName());
        response.setSupplierName(newServiceSupplier.getSupplierName());
        response.setSupplierAddress(newServiceSupplier.getSupplierAddress());
        response.setContactEmail(newServiceSupplier.getContactEmail());
        response.setContactPhone(newServiceSupplier.getContactPhone());
        response.setContactPersonName(newServiceSupplier.getContactPersonName());
        return response;
    }

}
