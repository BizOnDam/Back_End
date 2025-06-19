package com.bizondam.userservice.service;

import com.bizondam.userservice.entity.EmailAuth;
import com.bizondam.userservice.mapper.EmailAuthMapper;
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

  private final EmailAuthMapper emailAuthMapper;
  private final MailService mailService;

  public void createAndSendAuthCode(String email) {
    // 0. users 테이블에 이메일 존재 여부 확인
    if (emailAuthMapper.existsByEmail(email)) {
      log.warn("이메일 인증 요청 실패 - 이미 가입된 이메일 (email: {})", email);
      throw new IllegalArgumentException("이미 가입된 이메일입니다.");
    }

    // 1. 기존 인증 정보 삭제
    emailAuthMapper.deleteByEmail(email);

    // 2. 새 인증번호 생성 및 저장
    String code = generateRandomCode();
    EmailAuth emailAuth = new EmailAuth(null, email, code, LocalDateTime.now().plusMinutes(10));
    emailAuthMapper.insertEmailAuth(emailAuth);

    log.info("이메일 인증코드 생성 및 저장 (email: {}, code: {})", email, code);

    // 3. 이메일 발송
    mailService.send(email, "이메일 인증코드", "인증코드: " + code);
    log.info("이메일 인증코드 발송 완료 (email: {})", email);
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
}
