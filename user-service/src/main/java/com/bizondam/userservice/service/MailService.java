package com.bizondam.userservice.service;

public interface MailService {
  void send(String to, String subject, String content);
}