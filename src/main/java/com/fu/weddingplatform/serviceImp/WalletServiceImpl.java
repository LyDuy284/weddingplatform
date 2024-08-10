package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.wallet.WalletErrorMessage;
import com.fu.weddingplatform.constant.walletHistory.WalletHistoryConstant;
import com.fu.weddingplatform.constant.walletHistory.WalletHistoryType;
import com.fu.weddingplatform.entity.Account;
import com.fu.weddingplatform.entity.Wallet;
import com.fu.weddingplatform.entity.WalletHistory;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.AccountRepository;
import com.fu.weddingplatform.repository.WalletHistoryRepository;
import com.fu.weddingplatform.repository.WalletRepository;
import com.fu.weddingplatform.request.wallet.UpdateBalanceWallet;
import com.fu.weddingplatform.response.wallet.WalletResponse;
import com.fu.weddingplatform.service.WalletService;
import com.fu.weddingplatform.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
