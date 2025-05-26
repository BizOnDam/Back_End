package com.msa.configserver.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.Test;

public class JasyptConfigTest {
  @Test
  void printEncryptedValues() {
    StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
    enc.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
    enc.setPassword(System.getenv("ENCRYPTOR_PASSWORD"));
    enc.setIvGenerator(new RandomIvGenerator());

    String url      = "jdbc:mysql://localhost:3306/bizondam";
    String username = "root";
    String password = "1234";

    System.out.println("ENC(" + enc.encrypt(url) + ")");
    System.out.println("ENC(" + enc.encrypt(username) + ")");
    System.out.println("ENC(" + enc.encrypt(password) + ")");
  }
}
