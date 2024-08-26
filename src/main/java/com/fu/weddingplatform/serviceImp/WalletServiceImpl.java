package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.account.AccountErrorMessage;
import com.fu.weddingplatform.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.staff.StaffErrorMessage;
import com.fu.weddingplatform.constant.wallet.WalletErrorMessage;
import com.fu.weddingplatform.constant.walletHistory.WalletHistoryConstant;
import com.fu.weddingplatform.constant.walletHistory.WalletHistoryType;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.AccountRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.StaffRepository;
import com.fu.weddingplatform.repository.WalletHistoryRepository;
import com.fu.weddingplatform.repository.WalletRepository;
import com.fu.weddingplatform.request.wallet.TopUpWallet;
import com.fu.weddingplatform.request.wallet.UpdateBalanceWallet;
import com.fu.weddingplatform.response.wallet.WalletResponse;
import com.fu.weddingplatform.service.WalletService;
import com.fu.weddingplatform.utils.Utils;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    WalletHistoryRepository walletHistoryRepository;
    @Autowired
    private CoupleRepository coupleRepository;
    @Autowired
    private StaffRepository staffRepository;

    @Override
    public WalletResponse getBalanceWallet(int accountId) {
        Wallet wallet = walletRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ErrorException(WalletErrorMessage.NOT_FOUND));
        return modelMapper.map(wallet, WalletResponse.class);
    }

    @Override
    public Wallet updateBalanceWallet(UpdateBalanceWallet request) {
        Wallet wallet = walletRepository.findByAccountId(request.getAccountId())
                .orElseThrow(() -> new ErrorException(WalletErrorMessage.NOT_FOUND));
        WalletHistory walletHistory = WalletHistory.builder()
                .createDate(Utils.formatVNDatetimeNow())
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();
        if(request.getType().equals(WalletHistoryType.PlUS)){
            wallet.setBalance(wallet.getBalance() + request.getAmount());
            walletHistory.setWallet(wallet);
            walletHistory.setType(WalletHistoryType.PlUS);
        }else if(request.getType().equals(WalletHistoryType.MINUS)){
            if(wallet.getBalance() < request.getAmount()){
                throw new ErrorException(WalletErrorMessage.NOT_ENOUGH_BALANCE);
            }
            wallet.setBalance(wallet.getBalance() - request.getAmount());
            walletHistory.setWallet(wallet);
            walletHistory.setType(WalletHistoryType.MINUS);
        }
        Wallet walletSaved = walletRepository.save(wallet);
        walletHistoryRepository.save(walletHistory);
        return walletSaved;
    }

    @Override
    public WalletResponse topUpByStaff(TopUpWallet request) {
        Couple couple = coupleRepository.findById(request.getCoupleId())
                .orElseThrow(() ->  new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));
        UpdateBalanceWallet updateBalanceWallet = UpdateBalanceWallet.builder()
                .type(WalletHistoryType.PlUS)
                .accountId(couple.getAccount().getId())
                .amount(request.getAmount())
                .description(String.format(WalletHistoryConstant.DESCRIPTION_PLUS_MONEY, request.getAmount(), "ADMIN"))
                .build();
        Wallet wallet = updateBalanceWallet(updateBalanceWallet);
        return modelMapper.map(wallet, WalletResponse.class);
    }
}
