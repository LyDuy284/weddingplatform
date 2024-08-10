package com.fu.weddingplatform.service;

import java.util.List;

import javax.mail.MessagingException;

import com.fu.weddingplatform.response.booking.BookingDetailResponse;
import com.fu.weddingplatform.response.bookingHIstory.BookingDetailHistoryResponse;

public interface BookingDetailService {

  public BookingDetailResponse confirmBookingDetail(String bookingDetailId);

  public BookingDetailResponse rejectBookingDetail(String bookingDetailId) throws MessagingException;

  public BookingDetailResponse cancleBookingDetail(String bookingDetailId);

  public BookingDetailResponse depositBookingDetail(String bookingDetailId);

  public BookingDetailResponse completeBookingDetail(String bookingDetailId);

  public BookingDetailResponse processingBookingDetail(String bookingDetailId);

  public BookingDetailResponse doneBookingDetail(String bookingDetailId);

  public BookingDetailResponse finalPaymentBookingDetail(String bookingDetailId);

  public List<BookingDetailHistoryResponse> getBookingDetailHistoryById(String bookingDetailId);
}
