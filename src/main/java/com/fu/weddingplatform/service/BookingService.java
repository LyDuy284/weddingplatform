package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.request.booking.CreateBookingDTO;
import com.fu.weddingplatform.response.booking.BookingResponse;

public interface BookingService {
  public BookingResponse createBooking(CreateBookingDTO createDTO);

  public List<BookingResponse> getAllBookingBySupplier(String supplierId);

  // public List<BookingResponse> getAllBookingByCouple(String coupleId,
  // int pageNo,
  // int pageSize,
  // String sortBy,
  // boolean isAscending);

  public BookingResponse getBookingById(String bookingId);

  public BookingResponse convertBookingToBookingResponse(Booking booking);

  // public List<BookingStatusResponse> getBookingStatusById(String bookingId);

}
