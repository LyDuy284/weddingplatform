package com.fu.weddingplatform.request.payment;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

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
public class CreatePaymentDTO implements Serializable {
    @NotEmpty(message = "List Booking Detail Id " + ValidationMessage.NOT_EMPTY)
    List<String> listBookingDetailId;
    boolean isDeposit;
}
