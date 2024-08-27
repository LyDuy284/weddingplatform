package com.fu.weddingplatform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fu.weddingplatform.request.transactionSummary.TransactionSummaryResponse;
import com.fu.weddingplatform.response.statistic.DashboardStatistic;

public interface TransactionSummaryService {
    DashboardStatistic getStaffDashboardStatistic(int year);

    TransactionSummaryResponse getransactionSummary(String bookingId) throws JsonProcessingException;
}
