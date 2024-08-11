package com.fu.weddingplatform.service;

import javax.mail.MessagingException;

import com.fu.weddingplatform.entity.SentEmail;
import com.fu.weddingplatform.request.email.EmailBookingForCoupleDTO;
import com.fu.weddingplatform.request.email.EmailCreateBookingToSupplier;
import com.fu.weddingplatform.request.email.RejectMailDTO;

public interface SentEmailService {

  public void sentEmail(SentEmail sentEmail) throws MessagingException;

  public void sentBookingForCouple(EmailBookingForCoupleDTO emailBookingForCouple) throws MessagingException;

  public void sentBookingForSupplier(EmailCreateBookingToSupplier content) throws MessagingException;

  public void sentRejectBooking(RejectMailDTO rejectMailDTO) throws MessagingException;

}
