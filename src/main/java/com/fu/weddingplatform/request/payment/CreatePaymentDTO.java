package com.fu.weddingplatform.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fu.weddingplatform.constant.validation.ValidationMessage;
import com.fu.weddingplatform.enums.PaymentType;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentDTO implements Serializable {
    @NotEmpty(message = "List Booking Detail Id " + ValidationMessage.NOT_EMPTY)
    List<String> listBookingDetailId;
    boolean isDeposit;
}
