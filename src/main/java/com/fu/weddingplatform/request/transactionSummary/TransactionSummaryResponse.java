package com.fu.weddingplatform.request.transactionSummary;

import com.fu.weddingplatform.response.Account.SupplierResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TransactionSummaryResponse {
     String id;
     int totalAmount;
     String dateCreated;
     String dateModified;
     int platformFee;
     int supplierTotalEarn;
     String bookingId;
     Map<String, Integer> supplierAmountDetails;

}
