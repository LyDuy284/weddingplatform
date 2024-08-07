package com.fu.weddingplatform.response.transaction;

import com.fu.weddingplatform.entity.InvoiceDetail;
import com.fu.weddingplatform.response.invoiceDetail.InvoiceDetailResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    String id;
    String dateCreated;
    int amount;
    String transactionType;
    String status;
    InvoiceDetailResponse invoiceDetail;

}
