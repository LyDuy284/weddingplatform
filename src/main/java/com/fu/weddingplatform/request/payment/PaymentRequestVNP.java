package com.fu.weddingplatform.request.payment;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestVNP {
    Map<String, String> vnpParams;
    int amountVNPay;
    int amountWalletPaid;
}
