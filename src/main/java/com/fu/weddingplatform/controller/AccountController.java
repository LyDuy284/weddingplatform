package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.account.AccountSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.request.account.UpdateSupplierDTO;
import com.fu.weddingplatform.response.Account.AccountResponse;
import com.fu.weddingplatform.response.Account.SupplierResponse;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("getAllAccountByAdmin")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<?> getAllAccountByAdmin(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        ListResponseDTO<AccountResponse> responseDTO = new ListResponseDTO<>();
        List<AccountResponse> list = accountService.getAllUsersByAdmin(pageNo, pageSize);
        responseDTO.setData(list);
        responseDTO.setMessage(AccountSuccessMessage.GET_ALL_ACCOUNT);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getAllActivateUsersByAdmin")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<?> getAllActivateUsersByAdmin(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        ListResponseDTO<AccountResponse> responseDTO = new ListResponseDTO<>();
        List<AccountResponse> list = accountService.getAllActivateUsersByAdmin(pageNo, pageSize);
        responseDTO.setData(list);
        responseDTO.setMessage(AccountSuccessMessage.GET_ALL_ACTIVATE_ACCOUNT);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getAllAccountByRole")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<?> getAllAccountByRole(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize, @RequestParam String role) {
        ListResponseDTO<AccountResponse> responseDTO = new ListResponseDTO<>();
        List<AccountResponse> list = accountService.getAllAccountByRole(pageNo, pageSize, role);
        responseDTO.setData(list);
        responseDTO.setMessage(AccountSuccessMessage.GET_ALL_ACTIVATE_ACCOUNT);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("disableAccountByAdmin/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<?> disableAccountByAdmin(@RequestParam int id) {
        ResponseDTO<AccountResponse> responseDTO = new ResponseDTO<>();
        AccountResponse data = accountService.updateAccountStatus(id, Status.DISABLED);
        responseDTO.setData(data);
        responseDTO.setMessage(AccountSuccessMessage.DISABLE_ACCOUNT);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("activateAccountByAdmin/{id}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<?> activateAccountByAdmin(@RequestParam int id) {
        ResponseDTO<AccountResponse> responseDTO = new ResponseDTO<>();
        AccountResponse data = accountService.updateAccountStatus(id, Status.ACTIVATED);
        responseDTO.setData(data);
        responseDTO.setMessage(AccountSuccessMessage.ACTIVATE_ACCOUNT);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("updateSupplierProfile")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SUPPLIER)
    public ResponseEntity<?> updateSupplierProfile(@RequestBody UpdateSupplierDTO updateSupplierDTO) {
        ResponseDTO<SupplierResponse> responseDTO = new ResponseDTO<>();
        SupplierResponse data = accountService.updateSupplierProfile(updateSupplierDTO);
        responseDTO.setData(data);
        responseDTO.setMessage(AccountSuccessMessage.UPDATE_ACCOUNT);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
