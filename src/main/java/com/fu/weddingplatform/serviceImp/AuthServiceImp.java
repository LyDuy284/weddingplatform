package com.fu.weddingplatform.serviceImp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.crypto.SecretKey;

import com.fu.weddingplatform.constant.account.AccountProvider;
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
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.account.AccountErrorMessage;
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
        Account account = accountRepository.findByEmailAndProvider(loginDTO.getEmail(), AccountProvider.LOCAL)
                .orElseThrow(() -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_REGISTER));
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
                loginDTO.getPassword());
        LoginResponse loginResponse = null;
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            String token = Utils.buildJWT(authenticate, account, secretKey, jwtConfig);
             loginResponse = LoginResponse.builder()
                     .accountId(account.getId())
                     .email(account.getEmail())
                     .status(account.getStatus())
                     .roleName(account.getRole().getName())
                     .token(token)
                     .build();

        }
        return loginResponse;
    }

    @Override
    public AccountResponse registerNewAdmin(RegisterAdminDTO registerDTO) {
        Optional<Account> optionalUser = accountRepository.findByEmail(registerDTO.getEmail());

        Role role = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        AccountResponse response = new AccountResponse();

        if(optionalUser.isPresent()) {
            if (optionalUser.get().getProvider().equalsIgnoreCase(AccountProvider.LOCAL)) {
                throw new ErrorException(AccountErrorMessage.EXIST_EMAIL_ACCOUNT);
            } else {
                optionalUser.get().setPassword(passwordEncoder.encode(registerDTO.getPassword()));
                optionalUser.get().setName(registerDTO.getName());
                optionalUser.get().setProvider(AccountProvider.LOCAL);
                optionalUser.get().setPhoneNumber(registerDTO.getPhoneNumber());
                optionalUser.get().setAddress(registerDTO.getAddress());
                Account accountSaved = accountRepository.save(optionalUser.get());
                response = modelMapper.map(accountSaved, AccountResponse.class);
                return response;
            }
        }

        Account account = new Account().builder()
                .name(registerDTO.getName())
                .address(registerDTO.getAddress())
                .email(registerDTO.getEmail())
                .phoneNumber(registerDTO.getPhoneNumber())
                .role(role)
                .provider(AccountProvider.LOCAL)
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
        Optional<Account> optionalUser = accountRepository.findByEmail(registerDTO.getEmail());

        Role role = roleRepository.findByName(RoleName.ROLE_COUPLE)
                .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        RegsiterCoupleReponse response = new RegsiterCoupleReponse();
        Account accountSaved = new Account();
        if(optionalUser.isPresent()) {
            if (optionalUser.get().getProvider().equalsIgnoreCase(AccountProvider.LOCAL)) {
                throw new ErrorException(AccountErrorMessage.EXIST_EMAIL_ACCOUNT);
            } else {
                optionalUser.get().setPassword(passwordEncoder.encode(registerDTO.getPassword()));
                optionalUser.get().setName(registerDTO.getName());
                optionalUser.get().setProvider(AccountProvider.LOCAL);
                optionalUser.get().setPhoneNumber(registerDTO.getPhoneNumber());
                optionalUser.get().setAddress(registerDTO.getAddress());
                accountSaved = accountRepository.save(optionalUser.get());
            }
        } else {
            Account account = new Account().builder()
                .name(registerDTO.getName())
                .address(registerDTO.getAddress())
                .email(registerDTO.getEmail())
                .phoneNumber(registerDTO.getPhoneNumber())
                .provider(AccountProvider.LOCAL)
                .role(role)
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .status(Status.ACTIVATED)
                .build();
            accountSaved = accountRepository.save(account);
        }



        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate weddingDate = LocalDate.parse(registerDTO.getWeddingDate().toString(), dateFormatter);

        Couple couple = Couple.builder()
                .partnerName1(registerDTO.getPartnerName1())
                .partnerName2(registerDTO.getPartnerName2())
                .weddingDate(java.sql.Date.valueOf(weddingDate))
                .status(Status.STARTED)
                .account(accountSaved)
                .build();

        Couple newCouple = coupleRepository.save(couple);

        response = modelMapper.map(accountSaved, RegsiterCoupleReponse.class);

        response.setAccountId(accountSaved.getId());
        response.setRoleName(role.getName());
        response.setPartnerName1(couple.getPartnerName1());
        response.setPartnerName2(couple.getPartnerName2());
        response.setWeddingDate(newCouple.getWeddingDate().toString());
        response.setCoupleId(newCouple.getId());
        return response;
    }

    @Override
    public RegsiterStaffReponse registerStaff(RegisterStaffDTO registerDTO) {
        Optional<Account> optionalUser = accountRepository.findByEmail(registerDTO.getEmail());

        Role role = roleRepository.findByName(RoleName.ROLE_STAFF)
                .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        RegsiterStaffReponse response = new RegsiterStaffReponse();

        Account accountSaved = new Account();
        if(optionalUser.isPresent()) {
            if (optionalUser.get().getProvider().equalsIgnoreCase(AccountProvider.LOCAL)) {
                throw new ErrorException(AccountErrorMessage.EXIST_EMAIL_ACCOUNT);
            } else {
                optionalUser.get().setPassword(passwordEncoder.encode(registerDTO.getPassword()));
                optionalUser.get().setName(registerDTO.getName());
                optionalUser.get().setProvider(AccountProvider.LOCAL);
                optionalUser.get().setPhoneNumber(registerDTO.getPhoneNumber());
                optionalUser.get().setAddress(registerDTO.getAddress());
                accountSaved = accountRepository.save(optionalUser.get());
            }
        } else {
            Account account = new Account().builder()
                    .name(registerDTO.getName())
                    .address(registerDTO.getAddress())
                    .email(registerDTO.getEmail())
                    .phoneNumber(registerDTO.getPhoneNumber())
                    .provider(AccountProvider.LOCAL)
                    .role(role)
                    .password(passwordEncoder.encode(registerDTO.getPassword()))
                    .status(Status.ACTIVATED)
                    .build();
            accountSaved = accountRepository.save(account);
        }



        Staff staff = new Staff().builder()
                .account(accountSaved)
                .status(Status.ACTIVATED)
                .build();


        Staff newStaff = staffRepository.save(staff);

        response = modelMapper.map(accountSaved, RegsiterStaffReponse.class);

        response.setAccountId(accountSaved.getId());
        response.setRoleName(role.getName());
        response.setStaffId(newStaff.getId());
        return response;
    }

    @Override
    public RegsiterServiceSupplierReponse registerServiceSupplier(RegisterServiceSupplierDTO registerDTO) {
        Optional<Account> optionalUser = accountRepository.findByEmail(registerDTO.getEmail());

        Role role = roleRepository.findByName(RoleName.ROLE_SERVICE_SUPPLIER)
                .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        RegsiterServiceSupplierReponse response = new RegsiterServiceSupplierReponse();
        Account accountSaved = new Account();
        if(optionalUser.isPresent()) {
            if (optionalUser.get().getProvider().equalsIgnoreCase(AccountProvider.LOCAL)) {
                throw new ErrorException(AccountErrorMessage.EXIST_EMAIL_ACCOUNT);
            } else {
                optionalUser.get().setPassword(passwordEncoder.encode(registerDTO.getPassword()));
                optionalUser.get().setName(registerDTO.getName());
                optionalUser.get().setProvider(AccountProvider.LOCAL);
                optionalUser.get().setPhoneNumber(registerDTO.getPhoneNumber());
                optionalUser.get().setAddress(registerDTO.getAddress());
                accountSaved = accountRepository.save(optionalUser.get());
            }
        } else {
            Account account = new Account().builder()
                    .name(registerDTO.getName())
                    .address(registerDTO.getAddress())
                    .email(registerDTO.getEmail())
                    .phoneNumber(registerDTO.getPhoneNumber())
                    .provider(AccountProvider.LOCAL)
                    .role(role)
                    .password(passwordEncoder.encode(registerDTO.getPassword()))
                    .status(Status.ACTIVATED)
                    .build();
            accountSaved = accountRepository.save(account);
        }

        ServiceSupplier serviceSupplier = new ServiceSupplier()
                .builder()
                .supplierName(registerDTO.getSupplierName())
                .supplierAddress(registerDTO.getSupplierAddress())
                .contactPersonName(registerDTO.getContactPersonName())
                .contactPhone(registerDTO.getContactPhone())
                .contactEmail(registerDTO.getContactEmail())
                .status(Status.ACTIVATED)
                .build();


        ServiceSupplier newServiceSupplier = serviceSupplierRepository.save(serviceSupplier);

        response = modelMapper.map(accountSaved, RegsiterServiceSupplierReponse.class);

        response.setAccountId(accountSaved.getId());
        response.setRoleName(role.getName());
        response.setServiceSupplierId(newServiceSupplier.getId());
        response.setSupplierName(newServiceSupplier.getSupplierName());
        response.setSupplierAddress(newServiceSupplier.getSupplierAddress());
        response.setContactEmail(newServiceSupplier.getContactEmail());
        response.setContactPhone(newServiceSupplier.getContactPhone());
        response.setContactPersonName(newServiceSupplier.getContactPersonName());
        return response;
    }

    @Override
    public LoginResponse loginWithGoogle(String token) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();

        String[] split_string = token.split("\\.");
        String base64EncodedBody = split_string[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        JSONObject jsonObject = new JSONObject(body);
        String email = jsonObject.get("email").toString();
        String name = jsonObject.get("name").toString();

        Optional<Account> account = accountRepository.findByEmail(email);
        if(account.isEmpty()){
            account = Optional.of(registerForGoogleLogin(email, name, RoleName.ROLE_COUPLE));
            new Couple();
            Couple couple = Couple.builder().account(account.get()).status(Status.ACTIVATED).build();
            coupleRepository.save(couple);
        }
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(account.get().getRole().getName());
        simpleGrantedAuthorities.add(simpleGrantedAuthority);

        Authentication authentication = new UsernamePasswordAuthenticationToken(account.get().getEmail(), null);

        String tokenResponse = Jwts.builder().setSubject(authentication.getName())
                .claim(("authorities"), simpleGrantedAuthorities).claim("id", account.get().getId())
                .setIssuedAt((new Date())).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(14)))
                .signWith(jwtConfig.secretKey()).compact();

        return LoginResponse.builder()
                .accountId(account.get().getId())
                .email(account.get().getEmail())
                .status(account.get().getStatus())
                .roleName(account.get().getRole().getName())
                .token(tokenResponse)
                .build();
    }

    @Override
    public Account registerForGoogleLogin(String email, String name, String roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        Account account = new Account().builder()
                .name(name)
                .email(email)
                .role(role)
                .status(Status.ACTIVATED)
                .password(passwordEncoder.encode(""))
                .provider(AccountProvider.GOOGLE)
                .build();
        return accountRepository.save(account);
    }

}
