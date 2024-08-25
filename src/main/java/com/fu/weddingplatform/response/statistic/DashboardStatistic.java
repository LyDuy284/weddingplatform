package com.fu.weddingplatform.response.statistic;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatistic {
    int totalAmountCouplePaid;
    int totalAmountSupplierEarn;
    int totalAmountPlatformFee;
}
