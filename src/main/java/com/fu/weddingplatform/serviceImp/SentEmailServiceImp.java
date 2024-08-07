package com.fu.weddingplatform.serviceImp;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.email.EmailBookingForCouple;
import com.fu.weddingplatform.constant.email.RejectBookingDetail;
import com.fu.weddingplatform.constant.email.SentEmailBookingToSupplier;
import com.fu.weddingplatform.constant.email.Signature;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.SentEmail;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.SentEmailRepository;
import com.fu.weddingplatform.request.email.EmailBookingForCoupleDTO;
import com.fu.weddingplatform.request.email.EmailCreateBookingToSupplier;
import com.fu.weddingplatform.service.SentEmailService;
import com.fu.weddingplatform.utils.Utils;

@Service
@EnableScheduling
public class SentEmailServiceImp implements SentEmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private SentEmailRepository sentEmailRepository;

  // @Autowired
  // private TransactionSummaryRepository transactionSummaryRepository;

  // @Autowired
  // private BookingRepository bookingRepository;

  @Autowired
  private BookingDetailRepository bookingDetailRepository;

  @Override
  public void sentEmail(SentEmail sentEmail) throws MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
    mimeMessageHelper.setTo(sentEmail.getEmail());
    mimeMessageHelper.setSubject(sentEmail.getTitle());
    mimeMessageHelper.setText(sentEmail.getContent(), true);
    mimeMessageHelper.setFrom(String.format("\"%s\" <%s>", "The-Day-PlatForm", "weddingplatform176@gmail.com"));
    javaMailSender.send(mimeMessage);

    sentEmail.setStatus(Status.DONE);
    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentBookingForCouple(EmailBookingForCoupleDTO emailBookingForCoupleDTO) throws MessagingException {

    String content = EmailBookingForCouple.content(emailBookingForCoupleDTO);

    String title = "Đặt hàng thành công";
    SentEmail sentEmail = SentEmail.builder()
        .email(emailBookingForCoupleDTO.getEmail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();

    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentBookingForSupplier(EmailCreateBookingToSupplier emailCreate) throws MessagingException {

    String content = SentEmailBookingToSupplier.content(emailCreate);

    String title = "Xác nhận đơn hàng";
    SentEmail sentEmail = SentEmail.builder()
        .email(emailCreate.getEmail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();

    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentRejectBooking(BookingDetail rejectBookingDetail) throws MessagingException {

    String rejectService = "\t" + Utils.formatServiceDetail(rejectBookingDetail.getServiceSupplier().getName(),
        Utils.formatAmountToVND(rejectBookingDetail.getPrice()), rejectBookingDetail.getNote(),
        rejectBookingDetail.getCompletedDate())
        + "\n";

    String listService = "";

    List<BookingDetail> listBookingDetails = bookingDetailRepository
        .findValidBookingDetailByBooking(rejectBookingDetail.getBooking().getId());
    Booking booking = rejectBookingDetail.getBooking();
    for (BookingDetail bookingDetail : listBookingDetails) {
      String service = "\t" + Utils.formatServiceDetail(bookingDetail.getServiceSupplier().getName(),
          Utils.formatAmountToVND(bookingDetail.getPrice()), bookingDetail.getNote(),
          bookingDetail.getCompletedDate())
          + "\n";
      listService += service;
    }

    int paymentAmount = 0;
    // Utils.formatAmountToVND(booking.getTotalPrice());

    String content = String.format(RejectBookingDetail.content,
        rejectBookingDetail.getBooking().getCouple().getAccount().getName(), rejectBookingDetail.getServiceSupplier()
            .getName(),
        rejectBookingDetail.getId(),
        rejectBookingDetail.getCreateAt(), rejectService, booking.getId(), booking.getCreatedAt(), listService,
        Utils.formatAmountToVND(booking.getTotalPrice()),
        Utils.formatAmountToVND(paymentAmount),
        Utils.formatAmountToVND(booking.getTotalPrice() - paymentAmount));
    content += Signature.signature;

    String email = rejectBookingDetail.getServiceSupplier().getSupplier().getContactEmail();

    String title = "Từ chối đơn hàng";
    SentEmail sentEmail = SentEmail.builder()
        .email(email)
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();

    sentEmailRepository.save(sentEmail);
  }

  @Scheduled(cron = "*/5 * * * * *") // 5 second
  public void sendMailAuto() {
    SentEmail emailSchedule = sentEmailRepository.findFirstByStatus(Status.PENDING);

    try {
      if (emailSchedule != null) {
        sentEmail(emailSchedule);
        sentEmailRepository.delete(emailSchedule);
      }
    } catch (Exception e) {
      if (emailSchedule != null) {
        sentEmailRepository.delete(emailSchedule);
      }
      throw new RuntimeException(e);
    }
  }

}
