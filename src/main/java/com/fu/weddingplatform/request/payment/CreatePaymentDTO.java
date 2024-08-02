package com.fu.weddingplatform.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fu.weddingplatform.enums.PaymentType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentDTO implements Serializable {
    @JsonProperty("bookingId")
    List<String> listBookingDetailId;
    @JsonProperty("paymentType")
    PaymentType paymentType;
}
