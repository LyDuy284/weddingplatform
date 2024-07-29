package com.fu.weddingplatform.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fu.weddingplatform.enums.PaymentType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentDTO implements Serializable {
    @JsonProperty("bookingId")
    String bookingId;
    @JsonProperty("paymentType")
    PaymentType paymentType;
}
