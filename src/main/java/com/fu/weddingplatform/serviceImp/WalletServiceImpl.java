package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.wallet.WalletErrorMessage;
import com.fu.weddingplatform.entity.Wallet;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.WalletRepository;
import com.fu.weddingplatform.response.wallet.WalletResponse;
import com.fu.weddingplatform.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public WalletResponse getBalanceWallet(int accountId) {
        Wallet wallet = walletRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ErrorException(WalletErrorMessage.NOT_FOUND));
        return modelMapper.map(wallet, WalletResponse.class);
    }
}
