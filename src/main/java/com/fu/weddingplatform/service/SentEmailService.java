package com.fu.weddingplatform.service;

import javax.mail.MessagingException;

public interface SentEmailService {

  public void sentEmail(String email, String title, String content) throws MessagingException;

}
