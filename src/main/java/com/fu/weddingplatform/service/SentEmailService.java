package com.fu.weddingplatform.service;

import javax.mail.MessagingException;

import com.fu.weddingplatform.entity.SentEmail;
import com.fu.weddingplatform.request.email.CancelBookingDetailMailForCouple;
import com.fu.weddingplatform.request.email.CancelBookingMailForSupplierDTO;
import com.fu.weddingplatform.request.email.DepositedEmailForCouple;
import com.fu.weddingplatform.request.email.DepositedEmailForSupplierDTO;
import com.fu.weddingplatform.request.email.EmailBookingForCoupleDTO;
import com.fu.weddingplatform.request.email.EmailCreateBookingToSupplier;
import com.fu.weddingplatform.request.email.MailApproveForCoupleDTO;
import com.fu.weddingplatform.request.email.ProcessingMailForCoupleDTO;
import com.fu.weddingplatform.request.email.RejectMailDTO;

public interface SentEmailService {

  public void sentEmail(SentEmail sentEmail) throws MessagingException;

  public void sentBookingForCouple(EmailBookingForCoupleDTO emailBookingForCouple) throws MessagingException;

  public void sentBookingForSupplier(EmailCreateBookingToSupplier content) throws MessagingException;

  public void sentRejectBooking(RejectMailDTO rejectMailDTO) throws MessagingException;

  public void sentCancelBookingDetailForSupplier(CancelBookingMailForSupplierDTO cancelBookingMailForSupplierDTO);

  public void sentCancelBookingDetailForCouple(CancelBookingDetailMailForCouple cancelBookingDetailMailForCouple);

  public void sentDepositedEmailForSupplier(DepositedEmailForSupplierDTO depositedEmailForSupplierDTO);

  public void sentDepositedEmailForCouple(DepositedEmailForCouple depositedEmailForCouple);

  public void sentApprovedEmailForCouple(MailApproveForCoupleDTO mailApproveForCoupleDTO);

  public void sentProcessingEmailForCouple(ProcessingMailForCoupleDTO processingMailForCoupleDTO);

}
