package com.fu.weddingplatform.serviceImp;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.email.BookingByCouple;
import com.fu.weddingplatform.constant.email.BookingForSupplier;
import com.fu.weddingplatform.constant.email.RejectBookingDetail;
import com.fu.weddingplatform.constant.email.Signature;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.SentEmail;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.SentEmailRepository;
import com.fu.weddingplatform.service.SentEmailService;
import com.fu.weddingplatform.utils.Utils;

@Service
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
    mimeMessageHelper.setTo("dinhquanghuydt@gmail.com");
    // mimeMessageHelper.setTo(sentEmail.getEmail());
    mimeMessageHelper.setSubject(sentEmail.getTitle());
    mimeMessageHelper.setText(sentEmail.getContent());
    mimeMessageHelper.setFrom(String.format("\"%s\" <%s>", "The-Day-PlatForm", "weddingplatform176@gmail.com"));
    javaMailSender.send(mimeMessage);

    sentEmail.setStatus(Status.DONE);
    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentBookingForCouple(Booking booking) throws MessagingException {

    String listService = "";

    for (BookingDetail bookingDetail : booking.getBookingDetails()) {
      String service = Utils.formatServiceDetail(bookingDetail.getServiceSupplier().getName(),
          Utils.formatAmountToVND(bookingDetail.getPrice()), bookingDetail.getNote(), bookingDetail.getCompletedDate())
          + "\n";
      listService += service;
    }

    String totalPrice = Utils.formatAmountToVND(booking.getTotalPrice());

    String content = String.format(BookingByCouple.content, booking.getCouple().getAccount().getName(), booking.getId(),
        booking.getCreatedAt(), listService, totalPrice, Utils.formatAmountToVND(0), totalPrice);
    content += Signature.signature;

    String email = booking.getCouple().getAccount().getEmail();

    String title = "Đặt hàng thành công";
    SentEmail sentEmail = SentEmail.builder()
        .email(email)
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();

    sentEmailRepository.save(sentEmail);

  }

  @Override
  public void sentBookingForSupplier(BookingDetail bookingDetail) throws MessagingException {

    String service = "\t" + Utils.formatServiceDetail(bookingDetail.getServiceSupplier().getName(),
        Utils.formatAmountToVND(bookingDetail.getPrice()), bookingDetail.getNote(), bookingDetail.getCompletedDate())
        + "\n";

    String content = String.format(BookingForSupplier.content,
        bookingDetail.getServiceSupplier().getSupplier().getSupplierName(), bookingDetail.getId(),
        bookingDetail.getCreateAt(), service);
    content += Signature.signature;

    String email = bookingDetail.getServiceSupplier().getSupplier().getContactEmail();

    String title = "Xác nhận đơn hàng";
    SentEmail sentEmail = SentEmail.builder()
        .email(email)
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

}
