package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.response.booking.BookingDetailResponse;
import com.fu.weddingplatform.response.bookingHIstory.BookingDetailHistoryResponse;

public interface BookingDetailService {

  public BookingDetailResponse confirmBookingDetail(String bookingDetailId);

  public BookingDetailResponse rejectBookingDetail(String bookingDetailId);

  public BookingDetailResponse cancleBookingDetail(String bookingDetailId);

  public BookingDetailResponse completeBookingDetail(String bookingDetailId);

  public List<BookingDetailHistoryResponse> getBookingDetailHistoryById(String bookingDetailId);
}
