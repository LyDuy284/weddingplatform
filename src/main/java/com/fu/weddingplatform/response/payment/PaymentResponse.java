package com.fu.weddingplatform.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PaymentResponse {
    int amountPaidWallet;
    int amountPaymentVNPay;
    String urlPaymentVNPay;
}
