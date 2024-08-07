package com.fu.weddingplatform.service;

import javax.mail.MessagingException;

import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.SentEmail;
import com.fu.weddingplatform.request.email.EmailBookingForCoupleDTO;

public interface SentEmailService {

  public void sentEmail(SentEmail sentEmail) throws MessagingException;

  public void sentBookingForCouple(EmailBookingForCoupleDTO emailBookingForCouple) throws MessagingException;

  public void sentBookingForSupplier(BookingDetail bookingDetail) throws MessagingException;

  public void sentRejectBooking(BookingDetail bookingDetail) throws MessagingException;

}
