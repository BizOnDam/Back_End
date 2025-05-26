package com.msa.configserver.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

  @Bean("jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    // 1) 암호화·복호화 키를 환경 변수에서 가져옴
    config.setPassword(System.getenv("ENCRYPTOR_PASSWORD"));
    // 2) 알고리즘·옵션 설정
    config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256"); // 알고리즘
    config.setKeyObtentionIterations("1000"); // 반복할 해싱 회수
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
    config.setStringOutputType("base64"); //인코딩 방식
    config.setPoolSize("1"); // 인스턴스 pool
    encryptor.setConfig(config);
    return encryptor;
  }
}
