package com.bizondam.contract_service.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class GcpStorageConfig {     // GCS 설정

    @Value("${gcp.credentials.location}")   // 서비스 키 경로
    private Resource credentialsLocation;   // Resource 타입으로 선언해서 .getInputStream()을 통해 파일을 열 수 있게 함

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials   //  Google 인증 객체(GoogleCredentials) 생성
                .fromStream(credentialsLocation.getInputStream());

        // GCS 설정
        return StorageOptions.newBuilder()
                .setCredentials(credentials)    // 인증 정보 넣기
                .build()
                .getService();  //Google Cloud Storage 클라이언트 객체(Storage) 생성
    }
}
