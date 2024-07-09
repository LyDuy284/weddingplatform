package com.fu.weddingplatform.serviceImp;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.service.SentEmailService;

@Service
public class SentEmailServiceImp implements SentEmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Override
  public void sentEmail(String email, String title, String content) throws MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
    mimeMessageHelper.setTo(email);
    mimeMessageHelper.setSubject(title);
    mimeMessageHelper.setText(content);
    mimeMessageHelper.setFrom(String.format("\"%s\" <%s>", "Wedding-Platform", "weddingplatform176@gmail.com"));
    javaMailSender.send(mimeMessage);
  }

}
