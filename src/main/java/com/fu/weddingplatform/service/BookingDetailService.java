package com.fu.weddingplatform.service;

import com.fu.weddingplatform.entity.BookingDetail;

public interface BookingDetailService {
  public BookingDetail updateBookingServiceStatus(String bookingDetailId, String status);

  public BookingDetail confirmBookingService(String bookingDetailId);

  public BookingDetail rejectBookingService(String bookingDetailId);

  public BookingDetail cancleBookingService(String bookingDetailId);

  public BookingDetail doneBookingService(String bookingDetailId);

  public BookingDetail completeBookingService(String bookingDetailId);

  public BookingDetail processingBookingService(String bookingDetailId);
}
