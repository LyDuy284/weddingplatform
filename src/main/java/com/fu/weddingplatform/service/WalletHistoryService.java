package com.fu.weddingplatform.service;

import com.fu.weddingplatform.response.walletHistory.WalletHistoryResponse;

import java.time.LocalDate;
import java.util.List;

public interface WalletHistoryService {
    List<WalletHistoryResponse> getWalletHistory(String walletId, int pageNo, int pageSize, String sortBy,
                                                 boolean isAscending, LocalDate timeFrom, LocalDate timeTo, String type);
}
