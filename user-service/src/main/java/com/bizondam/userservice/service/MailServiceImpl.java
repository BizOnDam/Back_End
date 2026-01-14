package com.bizondam.userservice.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

  private final JavaMailSender javaMailSender;

  @Override
  public void send(String to, String subject, String content) {
    try {
      var message = javaMailSender.createMimeMessage();
      var helper = new MimeMessageHelper(message, false, "UTF-8");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(content, false);
      javaMailSender.send(message);
    } catch (Exception e) {
      throw new RuntimeException("이메일 발송 실패", e);
    }
  }
}