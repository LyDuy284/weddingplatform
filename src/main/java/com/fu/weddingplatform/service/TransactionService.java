package com.fu.weddingplatform.service;

import com.fu.weddingplatform.response.statistic.DashboardStatistic;
import com.fu.weddingplatform.response.transaction.TransactionResponse;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getCoupleTransactionsHistoryByFilter(String coupleId, int pageNo, int pageSize, String sortBy,
                                                                   boolean isAscending, LocalDate timeFrom, LocalDate timeTo, String isDeposit);
    TransactionResponse getTransactionById(String id);

    List<TransactionResponse> getBookingTransactionsHistoryByFilter(String bookingId, int pageNo, int pageSize, String sortBy,
                                                                   boolean isAscending, LocalDate timeFrom, LocalDate timeTo, String isDeposit);
    DashboardStatistic getSupplierDashboardStatistic(int month, int quarter, int year, String supplierId);
}
