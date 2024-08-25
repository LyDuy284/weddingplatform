package com.fu.weddingplatform.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
