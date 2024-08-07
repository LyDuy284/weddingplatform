package com.fu.weddingplatform.response.invoiceDetail;

import com.fu.weddingplatform.response.booking.BookingDetailResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetailResponse {
     String id;
     String status;
     boolean isDeposit;
     BookingDetailResponse bookingDetail;
}
