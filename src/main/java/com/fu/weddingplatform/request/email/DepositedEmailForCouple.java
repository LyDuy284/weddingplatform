package com.fu.weddingplatform.request.email;

import java.util.List;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.Couple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DepositedEmailForCouple {
    private List<BookingDetail> listBookingDetails;
    private Couple couple;
    private Booking booking;
    private String paymentAmount;
    private String totalAmount;
    private String remainingAmount;
}
