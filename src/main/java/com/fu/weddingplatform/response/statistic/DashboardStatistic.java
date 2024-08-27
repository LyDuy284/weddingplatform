package com.fu.weddingplatform.response.statistic;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatistic {
    int totalAmountCouplePaid;
    int totalAmountSupplierEarn;
    int totalAmountPlatformFee;
    Map<Integer, DashboardStatistic> amountEachMonths;
}
