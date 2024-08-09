package com.fu.weddingplatform.service;

import com.fu.weddingplatform.response.wallet.WalletResponse;

public interface WalletService {
    WalletResponse getBalanceWallet(int accountId);
}
