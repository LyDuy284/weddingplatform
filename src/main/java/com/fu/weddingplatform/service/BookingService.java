package com.fu.weddingplatform.service;

import java.util.List;

import javax.mail.MessagingException;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.request.booking.CreateBookingDTO;
import com.fu.weddingplatform.response.booking.BookingDetailBySupplierResponse;
import com.fu.weddingplatform.response.booking.BookingGroupBySupplierResponse;
import com.fu.weddingplatform.response.booking.BookingResponse;
import com.fu.weddingplatform.response.bookingHIstory.BookingHistoryResponse;

public interface BookingService {
  public BookingResponse createBooking(CreateBookingDTO createDTO) throws MessagingException;

  public List<BookingDetailBySupplierResponse> getAllBookingDetailBySupplierAndBookingId(String supplierId,
      String bookingId);

  public List<BookingResponse> getAllBookingByCouple(String coupleId);

  public BookingResponse getBookingById(String bookingId);

  public BookingResponse convertBookingToBookingResponse(Booking booking);

  public BookingResponse cancelBooking(String bookingId);

  public List<BookingGroupBySupplierResponse> getBookingBySupplier(String supplierId);

  public List<BookingHistoryResponse> getBookingHistoryById(String bookingId);

}
