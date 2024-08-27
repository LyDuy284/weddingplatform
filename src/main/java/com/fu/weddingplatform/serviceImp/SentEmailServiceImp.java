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
import com.fu.weddingplatform.constant.email.ApprovedMailForCouple;
import com.fu.weddingplatform.constant.email.CancelBookingDetailForCouple;
import com.fu.weddingplatform.constant.email.CancelBookingForSupplier;
import com.fu.weddingplatform.constant.email.DepositBookingForSupplier;
import com.fu.weddingplatform.constant.email.DepositBookingMailForCouple;
import com.fu.weddingplatform.constant.email.DoneMailForCouple;
import com.fu.weddingplatform.constant.email.EmailBookingForCouple;
import com.fu.weddingplatform.constant.email.ProccessingMailForCouple;
import com.fu.weddingplatform.constant.email.RefundForCouple;
import com.fu.weddingplatform.constant.email.RefundMailForSupplier;
import com.fu.weddingplatform.constant.email.RejectBookingDetail;
import com.fu.weddingplatform.constant.email.SentEmailBookingToSupplier;
import com.fu.weddingplatform.entity.SentEmail;
import com.fu.weddingplatform.repository.SentEmailRepository;
import com.fu.weddingplatform.request.email.CancelBookingDetailMailForCouple;
import com.fu.weddingplatform.request.email.CancelBookingMailForSupplierDTO;
import com.fu.weddingplatform.request.email.DepositedEmailForCouple;
import com.fu.weddingplatform.request.email.DepositedEmailForSupplierDTO;
import com.fu.weddingplatform.request.email.EmailBookingForCoupleDTO;
import com.fu.weddingplatform.request.email.EmailCreateBookingToSupplier;
import com.fu.weddingplatform.request.email.MailApproveForCoupleDTO;
import com.fu.weddingplatform.request.email.MailDoneForCoupleDTO;
import com.fu.weddingplatform.request.email.MailRefundForCoupleDTO;
import com.fu.weddingplatform.request.email.MailRefundForSupplierDTO;
import com.fu.weddingplatform.request.email.ProcessingMailForCoupleDTO;
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
    // mimeMessageHelper.setTo("lyhieuduy9190@gmail.com");
    mimeMessageHelper.setTo(sentEmail.getEmail());
    mimeMessageHelper.setSubject(sentEmail.getTitle());
    mimeMessageHelper.setText(sentEmail.getContent(), true);
    mimeMessageHelper.setFrom(String.format("\"%s\" <%s>", "The-Day-PlatForm", "weddingplatform176@gmail.com"));
    javaMailSender.send(mimeMessage);

    // sentEmail.setStatus(Status.DONE);
    sentEmailRepository.delete(sentEmail);
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
        // sentEmailRepository.delete(emailSchedule);
      }
    } catch (Exception e) {
      if (emailSchedule != null) {
        // sentEmailRepository.delete(emailSchedule);
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

  @Override
  public void sentCancelBookingDetailForCouple(CancelBookingDetailMailForCouple cancelBookingDetailMailForCouple) {
    String content = CancelBookingDetailForCouple.content(cancelBookingDetailMailForCouple);

    String title = "Đã hủy đơn hàng thành công";
    SentEmail sentEmail = SentEmail.builder()
        .email(cancelBookingDetailMailForCouple.getCoupleMail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();

    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentDepositedEmailForSupplier(DepositedEmailForSupplierDTO depositedEmailForSupplierDTO) {
    String content = DepositBookingForSupplier.content(depositedEmailForSupplierDTO);

    String title = "Đơn hàng đã được thanh toán cọc";
    SentEmail sentEmail = SentEmail.builder()
        .email(depositedEmailForSupplierDTO.getBookingDetail().getServiceSupplier().getSupplier().getContactEmail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();

    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentDepositedEmailForCouple(DepositedEmailForCouple depositedEmailForCouple) {

    String content = DepositBookingMailForCouple.content(depositedEmailForCouple);

    String title = "Đơn hàng đã được thanh toán cọc";
    SentEmail sentEmail = SentEmail.builder()
        .email(depositedEmailForCouple.getCouple().getAccount().getEmail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();

    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentApprovedEmailForCouple(MailApproveForCoupleDTO mailApproveForCoupleDTO) {
    String content = ApprovedMailForCouple.content(mailApproveForCoupleDTO);

    String title = "Đơn hàng đã được xác nhận";
    SentEmail sentEmail = SentEmail.builder()
        .email(mailApproveForCoupleDTO.getCouple().getAccount().getEmail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();

    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentProcessingEmailForCouple(ProcessingMailForCoupleDTO processingMailForCoupleDTO) {
    String content = ProccessingMailForCouple.content(processingMailForCoupleDTO);

    String title = "Đơn hàng của bạn đang được thực hiện";
    SentEmail sentEmail = SentEmail.builder()
        .email(processingMailForCoupleDTO.getCouple().getAccount().getEmail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();
    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentRefundEmailForSupplier(MailRefundForSupplierDTO mailRefundForSupplierDTO) {
    String content = RefundMailForSupplier.content(mailRefundForSupplierDTO);

    String title = "Đơn hàng đã được hoàn thành";
    SentEmail sentEmail = SentEmail.builder()
        .email(mailRefundForSupplierDTO.getCouple().getAccount().getEmail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();
    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentRefundEmailForCouple(MailRefundForCoupleDTO mailRefundForCoupleDTO) {
    String content = RefundForCouple.content(mailRefundForCoupleDTO);

    String title = "Tiền đã được hoàn về ví";
    SentEmail sentEmail = SentEmail.builder()
        .email(mailRefundForCoupleDTO.getCouple().getAccount().getEmail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();
    sentEmailRepository.save(sentEmail);
  }

  @Override
  public void sentDoneEmailForCouple(MailDoneForCoupleDTO mailDoneForCoupleDTO) {
    String content = DoneMailForCouple.content(mailDoneForCoupleDTO);

    String title = "Dịch vụ đã hoàn thành vui lòng thanh toán cho hệ thống";
    SentEmail sentEmail = SentEmail.builder()
        .email(mailDoneForCoupleDTO.getCouple().getAccount().getEmail())
        .content(content)
        .title(title)
        .status(Status.PENDING)
        .build();
    sentEmailRepository.save(sentEmail);
  }

}
