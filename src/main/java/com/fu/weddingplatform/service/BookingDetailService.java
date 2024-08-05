package com.fu.weddingplatform.service;

import com.fu.weddingplatform.response.booking.BookingDetailResponse;

public interface BookingDetailService {

  public BookingDetailResponse confirmBookingDetail(String bookingDetailId);

  public BookingDetailResponse rejectBookingDetail(String bookingDetailId);

  public BookingDetailResponse cancleBookingDetail(String bookingDetailId);

  public BookingDetailResponse completeBookingDetail(String bookingDetailId);

}
