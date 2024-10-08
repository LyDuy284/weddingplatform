package com.fu.weddingplatform.response.payment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVNPInfor {
    List<String> listInvoiceId;
    List<String> listVNPBookingDetailId;
    int vnpAmount;
    int amountPaidWallet;
}
