package com.bizondam.contract_service.util;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GcsUploader {

    private final Storage storage;  // GCP Storage Bean (GcpStorageConfig에서 주입)

    private static final String BUCKET_NAME = "bizondam-contracts"; // GCS에서 생성한 버킷 이름

    public String upload(String fileName, byte[] fileContent, String contentType) {
        log.info("GCS 업로드 시작 - bucket={}, fileName={}, contentType={}", BUCKET_NAME, fileName, contentType);

        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(BUCKET_NAME, fileName)
                    .setContentType(contentType)
                    .build();

            storage.create(blobInfo, fileContent);

            String fileUrl = String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, fileName);
            log.info("GCS 업로드 성공 - fileUrl={}", fileUrl);
            return fileUrl;

        } catch (Exception e) {
            log.error("GCS 업로드 실패 - fileName={}, message={}", fileName, e.getMessage(), e);
            throw new RuntimeException("GCS 업로드 중 오류가 발생했습니다.", e);
        }
    }
}
