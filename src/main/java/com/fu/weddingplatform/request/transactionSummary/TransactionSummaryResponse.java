package com.fu.weddingplatform.request.transactionSummary;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
     List<SupplierAmountDetails> supplierAmountDetails;

}
