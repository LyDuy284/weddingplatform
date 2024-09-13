package com.fu.weddingplatform.response.payment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVNPResponse {
    List<String> listInvoiceId;
    List<String> listBookingDetailId;
    boolean isDeposit;
}
