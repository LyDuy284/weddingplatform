package com.fu.weddingplatform.service;

import javax.mail.MessagingException;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.SentEmail;

public interface SentEmailService {

  public void sentEmail(SentEmail sentEmail) throws MessagingException;

  public void sentBookingForCouple(Booking booking) throws MessagingException;

  public void sentBookingForSupplier(BookingDetail bookingDetail) throws MessagingException;
}
