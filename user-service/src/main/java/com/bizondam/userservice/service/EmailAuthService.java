package com.bizondam.userservice.service;

import com.bizondam.userservice.entity.EmailAuth;
import com.bizondam.userservice.entity.User;
import com.bizondam.userservice.mapper.EmailAuthMapper;
import com.bizondam.userservice.mapper.UserMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EmailAuthService {
  private final UserMapper userMapper;
  private final EmailAuthMapper emailAuthMapper;
  private final MailService mailService;

  public void createAndSendAuthCodeForSignup(String email) {
    if (emailAuthMapper.existsByEmail(email)) {
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    }
    createAndSendAuthCode(email);
  }

  public void createAndSendAuthCodeForFindId(String email) {
    if (!emailAuthMapper.existsByEmail(email)) {
      throw new IllegalArgumentException("등록되지 않은 이메일입니다.");
    }
    createAndSendAuthCode(email);
  }

  private void createAndSendAuthCode(String email) {
    emailAuthMapper.deleteByEmail(email);
    String code = generateRandomCode();
    EmailAuth emailAuth = new EmailAuth(null, email, code, LocalDateTime.now().plusMinutes(10));
    emailAuthMapper.insertEmailAuth(emailAuth);
    mailService.send(email, "이메일 인증코드", "인증코드: " + code);
  }

  public boolean isVerified(String email, String code) {
    log.debug("입력 email: '{}', 입력 code: '{}'", email, code);
    EmailAuth auth = emailAuthMapper.findByEmailAndCode(email.trim(), code.trim());
    if (auth == null) {
      log.warn("이메일 인증 실패 - DB에 일치하는 인증 정보가 없음 (email: {}, code: {})", email, code);
      return false;
    }
    if (auth.getExpiredAt() == null || auth.isExpired()) {
      log.warn("이메일 인증 실패 - 인증코드 만료 (email: {}, code: {})", email, code);
      return false;
    }
    log.info("이메일 인증 성공 (email: {})", email);
    return true;
  }

  private String generateRandomCode() {
    int code = (int)(Math.random() * 900000) + 100000;
    return String.valueOf(code);
  }

  public boolean isVerifiedAndMatch(String email, String code, String loginId) {
    boolean verified = isVerified(email, code);
    if (!verified) return false;

    User user = userMapper.findByLoginId(loginId);
    return user != null && user.getEmail().equals(email);
  }
}
