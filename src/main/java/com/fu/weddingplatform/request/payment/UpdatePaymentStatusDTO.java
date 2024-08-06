package com.fu.weddingplatform.request.payment;

import com.fu.weddingplatform.constant.invoice.InvoiceStatus;
import com.fu.weddingplatform.constant.transaction.TransactionStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePaymentStatusDTO {
    String invoiceStatus;
    String invoiceDetailStatus;
    String transactionStatus;
}
