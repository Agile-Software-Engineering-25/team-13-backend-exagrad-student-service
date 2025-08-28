package com.ase.exagrad.studentservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Slf4j
public class MinioService {

  public void uploadFile(String bucketName, String objectKey, InputStream inputStream, long size, String contentType) {
    // Stub: Nur Logging
    log.info("Pretending to upload file to MinIO. bucket={}, key={}, size={}, contentType={}",
        bucketName, objectKey, size, contentType);

    // Später: Hier echten MinIO-Client aufrufen
  }

  public String getFileUrl(String bucketName, String objectKey) {
    // Stub: Gibt nur eine Fake-URL zurück
    String url = "http://fake-minio.local/" + bucketName + "/" + objectKey;
    log.info("Pretending to generate MinIO file URL: {}", url);
    return url;

    // Später: Pre-signed URL von MinIO zurückgeben
  }
}
