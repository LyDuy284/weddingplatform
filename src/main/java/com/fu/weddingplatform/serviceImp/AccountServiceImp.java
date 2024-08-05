package com.fu.weddingplatform.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.account.AccountErrorMessage;
import com.fu.weddingplatform.constant.role.RoleErrorMessage;
import com.fu.weddingplatform.constant.supplier.SupplierErrorMessage;
import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.entity.Area;
import com.fu.weddingplatform.entity.Role;
import com.fu.weddingplatform.entity.Supplier;
import com.fu.weddingplatform.exception.EmptyException;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.AccountRepository;
import com.fu.weddingplatform.repository.AreaRepository;
import com.fu.weddingplatform.repository.RoleRepository;
import com.fu.weddingplatform.repository.SupplierRepository;
import com.fu.weddingplatform.request.account.UpdateCoupleDTO;
import com.fu.weddingplatform.request.account.UpdateSupplierDTO;
import com.fu.weddingplatform.response.Account.AccountResponse;
import com.fu.weddingplatform.response.Account.SupplierResponse;
import com.fu.weddingplatform.service.AccountService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountServiceImp implements AccountService {

    private final AccountRepository accountRepository;
    private final SupplierRepository supplierRepository;
    // private final CoupleRepository coupleRepository;
    private final AreaRepository areaRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<AccountResponse> getAllUsersByAdmin(int pageNo, int pageSize) {
        List<AccountResponse> response = new ArrayList<AccountResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Account> pageResult = accountRepository.findAll(pageable);

        if (pageResult.hasContent()) {
            for (Account account : pageResult.getContent()) {
                AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
                accountResponse.setRoleName(account.getRole().getName());
                response.add(accountResponse);
            }
        } else {
            throw new EmptyException(AccountErrorMessage.EMPTY_ACCOUNT);
        }
        return response;
    }

    @Override
    public List<AccountResponse> getAllActivateUsersByAdmin(int pageNo, int pageSize) {
        List<AccountResponse> response = new ArrayList<AccountResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Account> pageResult = accountRepository.findByStatus(Status.ACTIVATED, pageable);

        if (pageResult.hasContent()) {
            for (Account account : pageResult.getContent()) {
                AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
                accountResponse.setRoleName(account.getRole().getName());
                response.add(accountResponse);
            }
        } else {
            throw new EmptyException(AccountErrorMessage.EMPTY_ACCOUNT);
        }
        return response;
    }

    @Override
    public List<AccountResponse> getAllAccountByRole(int pageNo, int pageSize, String roleName) {
        List<AccountResponse> response = new ArrayList<AccountResponse>();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

        Page<Account> pageResult = accountRepository.findByRole(role, pageable);

        if (pageResult.hasContent()) {
            for (Account account : pageResult.getContent()) {
                AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
                accountResponse.setRoleName(account.getRole().getName());
                response.add(accountResponse);
            }
        } else {
            throw new EmptyException(AccountErrorMessage.EMPTY_ACCOUNT);
        }
        return response;
    }

    @Override
    public AccountResponse updateAccountStatus(int id, String status) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));

        account.setStatus(status);
        Account accountSaved = accountRepository.save(account);

        AccountResponse response = modelMapper.map(accountSaved, AccountResponse.class);
        response.setRoleName(account.getRole().getName());
        return response;
    }

    @Override
    public SupplierResponse updateSupplierProfile(UpdateSupplierDTO updateDTO) {
        Supplier supplier = supplierRepository.findById(updateDTO.getSupplierId()).orElseThrow(
                () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

        Account account = supplier.getAccount();
        account.setName(updateDTO.getName());
        account.setImage(updateDTO.getImage());
        account.setPhoneNumber(updateDTO.getPhoneNumber());

        accountRepository.save(account);

        supplier.setContactPhone(updateDTO.getContactNumber());
        supplier.setContactEmail(updateDTO.getContactEmail());

        supplierRepository.save(supplier);
        Area area = new Area();
        if (supplier.getAreas().stream().findFirst().isEmpty()) {
            area = Area.builder()
                    .province(updateDTO.getProvince())
                    .district(updateDTO.getDistrict())
                    .ward(updateDTO.getWard())
                    .apartmentNumber(updateDTO.getApartmentNumber())
                    // .supplier(supplier)
                    .status(Status.ACTIVATED)
                    .build();
        } else {
            supplier.getAreas().stream().findFirst().get();
            area.setProvince(updateDTO.getProvince());
            area.setDistrict(updateDTO.getDistrict());
            area.setWard(updateDTO.getWard());
            area.setApartmentNumber(updateDTO.getApartmentNumber());
            area.setSupplier(supplier);
        }

        Area areaSaved = areaRepository.save(area);

        SupplierResponse response = new SupplierResponse();
        response.setSupplierName(account.getName());
        response.setImage(account.getImage());
        response.setContactEmail(supplier.getContactEmail());
        response.setContactPhone(supplier.getContactPhone());
        response.setArea(areaSaved);
        return response;
    }

    @Override
    public AccountResponse updateCoupleProfile(UpdateCoupleDTO updateCoupleDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCoupleProfile'");
    }
}
