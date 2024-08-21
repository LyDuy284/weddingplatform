package com.fu.weddingplatform.service;

import com.fu.weddingplatform.entity.Wallet;
import com.fu.weddingplatform.request.wallet.TopUpWallet;
import com.fu.weddingplatform.request.wallet.UpdateBalanceWallet;
import com.fu.weddingplatform.response.wallet.WalletResponse;

public interface WalletService {
    WalletResponse getBalanceWallet(int accountId);
    Wallet updateBalanceWallet(UpdateBalanceWallet request);

    WalletResponse topUpByStaff(TopUpWallet request);
}
