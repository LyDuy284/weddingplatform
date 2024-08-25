package com.fu.weddingplatform.response.booking;

import com.fu.weddingplatform.response.couple.CoupleResponse;
import com.fu.weddingplatform.response.promotion.PromotionResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierBySupplierBooking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookingDetailBySupplierResponse {
    private CoupleResponse couple;
    private String bookingDetailId;
    private ServiceSupplierBySupplierBooking serviceSupplierResponse;
    private int price;
    private int quantity;
    private String note;
    private String createAt;
    private String weddingDate;
    private String completedDate;
    private String status;
    private PromotionResponse promotionResponse;
}
