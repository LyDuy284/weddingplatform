package com.fu.weddingplatform.response.booking;

import java.util.List;

import com.fu.weddingplatform.response.couple.CoupleResponse;

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
public class BookingResponse {
  private String id;
  private String weddingDate;
  private CoupleResponse couple;
  private List<BookingDetailResponse> listBookingDetail;
  private String note;
  private int totalPrice;
  private String createdAt;
  private String status;
}
