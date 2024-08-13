package com.fu.weddingplatform.response.payment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVNPInfor {
    List<String> listVNPBookingDetailId;
    int vnpAmount;
}
