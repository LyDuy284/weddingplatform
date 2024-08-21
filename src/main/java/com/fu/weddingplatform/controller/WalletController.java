package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.constant.wallet.WalletSuccessMessage;
import com.fu.weddingplatform.request.wallet.TopUpWallet;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.wallet.WalletResponse;
import com.fu.weddingplatform.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("wallet")
@CrossOrigin("*")
@RequiredArgsConstructor
public class WalletController {

    @Autowired
    WalletService walletService;

    @GetMapping("balance/{accountId}")
    public ResponseEntity<?> getBalanceWallet(@PathVariable int accountId){
        WalletResponse walletResponse = walletService.getBalanceWallet(accountId);
        ResponseDTO<WalletResponse>  responseDTO = new ResponseDTO<>();
        responseDTO.setMessage(WalletSuccessMessage.GET);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setData(walletResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("topUpByStaff")
    @PreAuthorize(RolePreAuthorize.ROLE_STAFF)
    public ResponseEntity<?> topUpWalletByStaff(@RequestBody TopUpWallet request){
        WalletResponse walletResponse = walletService.topUpByStaff(request);
        ResponseDTO<WalletResponse>  responseDTO = new ResponseDTO<>();
        responseDTO.setMessage(WalletSuccessMessage.TOPUP);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setData(walletResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
