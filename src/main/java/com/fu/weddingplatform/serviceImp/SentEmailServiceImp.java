package com.fu.weddingplatform.serviceImp;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.email.CancelBookingForSupplier;
import com.fu.weddingplatform.constant.email.EmailBookingForCouple;
import com.fu.weddingplatform.constant.email.RejectBookingDetail;
import com.fu.weddingplatform.constant.email.SentEmailBookingToSupplier;
import com.fu.weddingplatform.entity.SentEmail;
import com.fu.weddingplatform.repository.SentEmailRepository;
import com.fu.weddingplatform.request.email.CancelBookingMailForSupplierDTO;
import com.fu.weddingplatform.request.email.EmailBookingForCoupleDTO;
import com.fu.weddingplatform.request.email.EmailCreateBookingToSupplier;
import com.fu.weddingplatform.request.email.RejectMailDTO;
import com.fu.weddingplatform.service.SentEmailService;

@Service
@EnableScheduling
public class SentEmailServiceImp implements SentEmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private SentEmailRepository sentEmailRepository;

  @Override
  public void sentEmail(SentEmail sentEmail) throws MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
    mimeMessageHelper.setTo("dinhquanghuydt@gmail.com");
    // mimeMessageHelper.setTo(sentEmail.getEmail());
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

  @Override
  public void sentRejectBooking(RejectMailDTO rejectMailDTO) throws MessagingException {
    String content = RejectBookingDetail.content(rejectMailDTO);

    String title = "Đơn hàng bị từ chối";
    SentEmail sentEmail = SentEmail.builder()
        .email(rejectMailDTO.getMail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();

    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentCancelBookingDetailForSupplier(CancelBookingMailForSupplierDTO cancelBookingMailForSupplierDTO) {

    String content = CancelBookingForSupplier.content(cancelBookingMailForSupplierDTO);

    String title = "Đơn hàng bị huỷ";
    SentEmail sentEmail = SentEmail.builder()
        .email(cancelBookingMailForSupplierDTO.getSupplierEmail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();

    sentEmailRepository.save(sentEmail);
  }

}
