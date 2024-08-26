package com.fu.weddingplatform.service;

import com.fu.weddingplatform.entity.TransactionSummary;
import com.fu.weddingplatform.request.transactionSummary.TransactionSummaryResponse;
import com.fu.weddingplatform.response.statistic.DashboardStatistic;

public interface TransactionSummaryService {
    DashboardStatistic getStaffDashboardStatistic(int month, int quarter, int year);

    TransactionSummaryResponse gettransactionSummary(String bookingId);
}
